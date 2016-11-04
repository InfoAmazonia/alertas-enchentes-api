library(dplyr)
library(tidyr)
library(reshape2)
library(zoo)

months <- c('Jan','Feb','Mar',
            'Apr','May','Jun',
            'Jul','Aug','Sep',
            'Oct','Nov','Dec')

args <- commandArgs(TRUE)

raw_data <- read.csv(file = 'data/raw/14990000.csv', skip = 1, header = T)
#raw_data <- read.csv(file = args[1], skip = 1, header = T)

cod <- raw_data[1,1]

raw_data <- raw_data[,c(15, 22:52)]
names(raw_data) <- c('date_as_string', 1:31)

dec99 <- as.numeric(rownames(raw_data[match('Dec-99',raw_data$date_as_string),]))
raw_data$baseyear <- 1900
raw_data[(dec99+1):nrow(raw_data), ]$baseyear <- 2000

raw_data <- raw_data %>% 
  separate(date_as_string, sep = "-", into=c('month', 'year')) %>% 
  mutate(year = as.numeric(year), year = baseyear + year) %>%
  mutate(month = match(month, months))

raw_data$baseyear <- NULL

raw_data <- melt(raw_data, id.vars= c('month', 'year'), variable.name = 'day', value.name = 'value')

raw_data <- 
  raw_data %>% 
  mutate(timestamp = paste(month, day, year, sep = '/'), timestamp = as.POSIXct(timestamp,format='%m/%d/%Y')) %>% 
  filter(!is.na(timestamp)) %>%
  select(timestamp, value) %>%
  arrange(timestamp) %>%
  distinct(timestamp, .keep_all = TRUE)

ts <- seq.POSIXt(head(raw_data, 1)$timestamp, tail(raw_data, 1)$timestamp, by="DSTday")
df <- data.frame(timestamp=ts)
data_with_missing_times <- full_join(df,raw_data, by='timestamp')

write.table(file = paste('data/history/', cod, '.csv', sep=''), x = data_with_missing_times, quote = TRUE, sep = ',', row.names = F, col.names = F)
