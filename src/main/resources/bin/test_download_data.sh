#!/bin/bash

output_dir=$1

if [ "$(uname)" == "Darwin" ]; then
	command -v gdate >/dev/null 2>&1 || { echo >&2 "I require gdate but it's not installed. Try 'brew install coreutils'"; exit 1; }
	dayini=$(gdate --date="1 day ago" +%d)
	monthini=$(gdate --date="1 day ago" +%m)
	yearini=$(gdate --date="1 day ago" +%Y)
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
	dayini=$(date --date="1 day ago" +%d)
	monthini=$(date --date="1 day ago" +%m)
	yearini=$(date --date="1 day ago" +%Y)
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
	echo >&2 "Unsupported architecture"; exit 1;
fi

day=$(date +%d)
month=$(date +%m)
year=$(date +%Y)


echo $day $dayini $month $year

curl -o "${output_dir}/13551000" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDEzNTUxMDAwIFhBUFVSSSAtIFBDRDs%2BO0A8MTAzODY4MzAwOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCOz47bDxJbmZvcm1lIGEgImVzdGHDp8OjbyIgLjsyPFJlZD47aTw0Pjs%2BPjs%2BOzs%2BO3Q8dDw7dDxpPDI%2BO0A8Q2h1dmE6IDAxLzEyLzIwMTQgLSAyOC8wOC8yMDE2O07DrXZlbDogMDEvMTIvMjAxNCAtIDI4LzA4LzIwMTY7PjtAPDE7Mjs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs%2BO0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU%2BOz4%2BOzs%2BO3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs%2BO0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE%2BO0A8XDx0b2Rvc1w%2BOzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDQ%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTM1NTEwMDA7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj5PU8MpuI6MRVrTHYBL6h61B6gd2Q==&lstBacia=1&lstDisponivel=2&lstEstacao=103868300&lstOrigem=5&lstSubBacia=13&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=13551000&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}/13600002" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwyPjtAPDEzNjAwMDAyIFJJTyBCUkFOQ087MTM2MDAwMDIgUklPIEJSQU5DTzs%2BO0A8OTU5Njc0ODA7OTU4Njc0ODA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPFxlOzI8UmVkPjtpPDQ%2BOz4%2BOz47Oz47dDx0PDt0PGk8Mz47QDxDaHV2YTogMDEvMTIvMjAxNCAtIDAzLzExLzIwMTY7TsOtdmVsOiAwMS8xMi8yMDE0IC0gMDMvMTEvMjAxNjtWYXrDo286IDAxLzEyLzIwMTQgLSAwMy8xMS8yMDE2Oz47QDwxOzI7Mzs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs%2BO0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU%2BOz4%2BOzs%2BO3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs%2BO0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE%2BO0A8XDx0b2Rvc1w%2BOzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDQ%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTM2MDAwMDI7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj6hfOMdNBJIW8nXsAHG2o/aVK2emw==&lstBacia=1&lstDisponivel=2&lstEstacao=95867480&lstOrigem=5&lstSubBacia=13&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=13600002&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}15320002" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDE1MzIwMDAyIEFCVU7Dgzs%2BO0A8OTQyNjUyMTI7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPFxlOzI8UmVkPjtpPDQ%2BOz4%2BOz47Oz47dDx0PDt0PGk8Mz47QDxDaHV2YTogMjkvMTAvMjAxNCAtIDMwLzA4LzIwMTY7TsOtdmVsOiAyOS8xMC8yMDE0IC0gMzAvMDgvMjAxNjtWYXrDo286IDI2LzAxLzIwMTUgLSAzMC8wOC8yMDE2Oz47QDwxOzI7Mzs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs%2BO0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU%2BOz4%2BOzs%2BO3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs%2BO0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE%2BO0A8XDx0b2Rvc1w%2BOzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDY%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTUzMjAwMDI7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj7nSkiytjQmd3UDcp0JTRVBUYx2QQ==&lstBacia=1&lstDisponivel=2&lstEstacao=94265212&lstOrigem=5&lstSubBacia=15&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=15320002&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}15326000" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDE1MzI2MDAwIE1PUkFEQSBOT1ZBIC0gSlVTQU5URTs%2BO0A8OTQ3NjUzMjA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPFxlOzI8UmVkPjtpPDQ%2BOz4%2BOz47Oz47dDx0PDt0PGk8Mz47QDxDaHV2YTogMjkvMTAvMjAxNCAtIDMwLzA4LzIwMTY7TsOtdmVsOiAyOS8xMC8yMDE0IC0gMzAvMDgvMjAxNjtWYXrDo286IDI2LzAxLzIwMTUgLSAzMC8wOC8yMDE2Oz47QDwxOzI7Mzs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs%2BO0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU%2BOz4%2BOzs%2BO3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs%2BO0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE%2BO0A8XDx0b2Rvc1w%2BOzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDY%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTUzMjYwMDA7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj7QYWuz90SGm5zfOuqFB6MyrkjVPA==&lstBacia=1&lstDisponivel=2&lstEstacao=94765320&lstOrigem=5&lstSubBacia=15&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=15326000&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}15250000" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDE1MjUwMDAwIEdVQUpBUsOBLU1JUklNOz47QDwxMDQ4NjUyMTA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPEluZm9ybWUgYSAiZXN0YcOnw6NvIiAuOzI8UmVkPjtpPDQ%2BOz4%2BOz47Oz47dDx0PDt0PGk8Mz47QDxDaHV2YTogMjkvMTAvMjAxNCAtIDMwLzA4LzIwMTY7TsOtdmVsOiAyOS8xMC8yMDE0IC0gMzAvMDgvMjAxNjtWYXrDo286IDI2LzAxLzIwMTUgLSAzMC8wOC8yMDE2Oz47QDwxOzI7Mzs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8Nj47QDxcPHRvZG9zXD47QU5BL0lOUEU7QU5BL1NJVkFNO1NldG9yIEVsw6l0cmljbztDb3RhT25saW5lO1Byb2pldG9zX0VzcGVjaWFpczs%2BO0A8MDsxOzI7Mzs0OzU7Pj47bDxpPDU%2BOz4%2BOzs%2BO3Q8dDw7dDxpPDEwPjtAPFw8dG9kb3NcPjsxIFJpbyBBbWF6b25hczsyIFJpbyBUb2NhbnRpbnM7MyBBdGzDom50aWNvLCBUcmVjaG8gTm9ydGUvTm9yZGVzdGU7NCBSaW8gU8OjbyBGcmFuY2lzY287NSBBdGzDom50aWNvLCBUcmVjaG8gTGVzdGU7NiBSaW8gUGFyYW7DocKgOzcgUmlvIFVydWd1YWk7OCBBdGzDom50aWNvLCBUcmVjaG8gU3VkZXN0ZTs5IE91dHJhczs%2BO0A8MDsxOzI7Mzs0OzU7Njs3Ozg7OTs%2BPjtsPGk8MT47Pj47Oz47dDx0PDt0PGk8MTE%2BO0A8XDx0b2Rvc1w%2BOzEwIFJJTyBTT0xJTU9FUywgSkFWQVJJLElUQUNVQUk7MTEgUklPIFNPTElNT0VTLElDQSxKQU5ESUFUVUJBLC4uOzEyIFJJTyBTT0xJTU9FUyxKVVJVQSxKQVBVUkEsLi4uLjsxMyBSSU8gU09MSU1PRVMsUFVSVVMsQ09BUkksLi47MTQgUklPIFNPTElNT0VTLE5FR1JPLEJSQU5DTywuLi4uOzE1IFJJTyBBTUFaT05BUyxNQURFSVJBLEdVQVBPUkUsLjsxNiBSSU8gQU1BWk9OQVMsVFJPTUJFVEFTLE9VVFJPUzsxNyBSSU8gQU1BWk9OQVMsVEFQQUpPUyxKVVJVRU5BLi47MTggUklPIEFNQVpPTkFTLFhJTkdVLElSSVJJLFBBUlU7MTkgUklPIEFNQVpPTkFTLEpBUkksUEFSQSxPVVRST1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7Pj47bDxpPDY%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTUyNTAwMDA7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj6ys62%2BLPYdvDsJh0dNtALxgrDdYQ==&lstBacia=1&lstDisponivel=2&lstEstacao=104865210&lstOrigem=5&lstSubBacia=15&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=15250000&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}15400000" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDE1NDAwMDAwIFBPUlRPIFZFTEhPOz47QDw4NDg2MzU3MDs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8SW5mb3JtZSBhICJlc3Rhw6fDo28iIC47MjxSZWQ%2BO2k8ND47Pj47Pjs7Pjt0PHQ8O3Q8aTwzPjtAPENodXZhOiAyOS8xMC8yMDE0IC0gMzAvMDgvMjAxNjtOw612ZWw6IDI5LzEwLzIwMTQgLSAzMC8wOC8yMDE2O1ZhesOjbzogMjYvMDEvMjAxNSAtIDMwLzA4LzIwMTY7PjtAPDE7MjszOz4%2BO2w8aTwxPjs%2BPjs7Pjt0PHQ8O3Q8aTw2PjtAPFw8dG9kb3NcPjtBTkEvSU5QRTtBTkEvU0lWQU07U2V0b3IgRWzDqXRyaWNvO0NvdGFPbmxpbmU7UHJvamV0b3NfRXNwZWNpYWlzOz47QDwwOzE7MjszOzQ7NTs%2BPjtsPGk8NT47Pj47Oz47dDx0PDt0PGk8MTA%2BO0A8XDx0b2Rvc1w%2BOzEgUmlvIEFtYXpvbmFzOzIgUmlvIFRvY2FudGluczszIEF0bMOibnRpY28sIFRyZWNobyBOb3J0ZS9Ob3JkZXN0ZTs0IFJpbyBTw6NvIEZyYW5jaXNjbzs1IEF0bMOibnRpY28sIFRyZWNobyBMZXN0ZTs2IFJpbyBQYXJhbsOhwqA7NyBSaW8gVXJ1Z3VhaTs4IEF0bMOibnRpY28sIFRyZWNobyBTdWRlc3RlOzkgT3V0cmFzOz47QDwwOzE7MjszOzQ7NTs2Ozc7ODs5Oz4%2BO2w8aTwxPjs%2BPjs7Pjt0PHQ8O3Q8aTwxMT47QDxcPHRvZG9zXD47MTAgUklPIFNPTElNT0VTLCBKQVZBUkksSVRBQ1VBSTsxMSBSSU8gU09MSU1PRVMsSUNBLEpBTkRJQVRVQkEsLi47MTIgUklPIFNPTElNT0VTLEpVUlVBLEpBUFVSQSwuLi4uOzEzIFJJTyBTT0xJTU9FUyxQVVJVUyxDT0FSSSwuLjsxNCBSSU8gU09MSU1PRVMsTkVHUk8sQlJBTkNPLC4uLi47MTUgUklPIEFNQVpPTkFTLE1BREVJUkEsR1VBUE9SRSwuOzE2IFJJTyBBTUFaT05BUyxUUk9NQkVUQVMsT1VUUk9TOzE3IFJJTyBBTUFaT05BUyxUQVBBSk9TLEpVUlVFTkEuLjsxOCBSSU8gQU1BWk9OQVMsWElOR1UsSVJJUkksUEFSVTsxOSBSSU8gQU1BWk9OQVMsSkFSSSxQQVJBLE9VVFJPUzs%2BO0A8MDsxMDsxMTsxMjsxMzsxNDsxNTsxNjsxNzsxODsxOTs%2BPjtsPGk8Nj47Pj47Oz47dDxwPHA8bDxUZXh0Oz47bDwxNTQwMDAwMDs%2BPjs%2BOzs%2BOz4%2BOz4%2BO2w8bHN0RXN0YWNhbztidEF0dWFsaXphcjtsc3REaXNwb25pdmVsO2xzdE9yaWdlbTtsc3RCYWNpYTtsc3RTdWJCYWNpYTs%2BPhMI6KzKstHjd3G4evnPNyD8djRI&lstBacia=1&lstDisponivel=2&lstEstacao=84863570&lstOrigem=5&lstSubBacia=15&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=15400000&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
curl -o "${output_dir}14990000" -X POST -H "Origin: http://mapas-hidro.ana.gov.br" -H "Content-Type: application/x-www-form-urlencoded" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" -H "Referer: http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx" -H "Accept-Encoding: gzip, deflate" -H "Accept-Language: en-US,en;q=0.8,pt;q=0.6,es;q=0.4" -H "Cookie: ASP.NET_SessionId=s5ao1se3shpr0ifzfepsxybh; ASP.NET_SessionId=k5uozzqjxu0sfc55uzvbsd55" -H "Cache-Control: no-cache" -H "Postman-Token: 15b6fba9-a6bf-37c3-9c67-1ce79e47ce74" -d "__EVENTARGUMENT=&__EVENTTARGET=btGerar&__VIEWSTATE=dDw0MDI2NjM1MTE7dDw7bDxpPDE%2BOz47bDx0PDtsPGk8NT47aTwxMT47aTwxMj47aTwxMz47aTwxND47aTwxNj47aTwxOD47aTwyMT47aTwyOT47aTw0OD47aTw0OT47aTw1Mj47aTw1ND47PjtsPHQ8cDxwPGw8VGV4dDtGb3JlQ29sb3I7XyFTQjs%2BO2w8IHxTw6lyaWUgaGlzdMOzcmljYSB8OzI8MCwgMCwgMTIwPjtpPDQ%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPHwgUGVzcXVpc2FyIERhZG9zIHw7aTwyNTY%2BOzE8MTE1cHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O0ZvcmVDb2xvcjtfIVNCO1dpZHRoO0JhY2tDb2xvcjs%2BO2w8fCBFeHBvcnRhciBEYWRvcyB8OzI8V2hpdGU%2BO2k8MjY4PjsxPDEwNXB4PjsyPDAsIDAsIDEyMD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7XyFTQjtXaWR0aDs%2BO2w8XGU7aTwyNTY%2BOzE8MHB4Pjs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDtfIVNCO1dpZHRoOz47bDxcZTtpPDI1Nj47MTwwcHg%2BOz4%2BOz47Oz47dDxwPHA8bDxUZXh0O18hU0I7V2lkdGg7PjtsPFxlO2k8MjU2PjsxPDBweD47Pj47Pjs7Pjt0PHQ8O3Q8aTwxPjtAPDE0OTkwMDAwIE1BTkFVUzs%2BO0A8MzA2NTk2MDA7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7Rm9yZUNvbG9yO18hU0I7PjtsPFxlOzI8UmVkPjtpPDQ%2BOz4%2BOz47Oz47dDx0PDt0PGk8Mj47QDxDaHV2YTogMjkvMDYvMjAwNSAtIDMwLzA5LzIwMTY7TsOtdmVsOiAzMC8wNi8yMDA1IC0gMzAvMDkvMjAxNjs%2BO0A8MTsyOz4%2BO2w8aTwxPjs%2BPjs7Pjt0PHQ8O3Q8aTw2PjtAPFw8dG9kb3NcPjtBTkEvSU5QRTtBTkEvU0lWQU07U2V0b3IgRWzDqXRyaWNvO0NvdGFPbmxpbmU7UHJvamV0b3NfRXNwZWNpYWlzOz47QDwwOzE7MjszOzQ7NTs%2BPjtsPGk8NT47Pj47Oz47dDx0PDt0PGk8MTA%2BO0A8XDx0b2Rvc1w%2BOzEgUmlvIEFtYXpvbmFzOzIgUmlvIFRvY2FudGluczszIEF0bMOibnRpY28sIFRyZWNobyBOb3J0ZS9Ob3JkZXN0ZTs0IFJpbyBTw6NvIEZyYW5jaXNjbzs1IEF0bMOibnRpY28sIFRyZWNobyBMZXN0ZTs2IFJpbyBQYXJhbsOhwqA7NyBSaW8gVXJ1Z3VhaTs4IEF0bMOibnRpY28sIFRyZWNobyBTdWRlc3RlOzkgT3V0cmFzOz47QDwwOzE7MjszOzQ7NTs2Ozc7ODs5Oz4%2BO2w8aTwwPjs%2BPjs7Pjt0PHQ8O3Q8aTw4MT47QDxcPHRvZG9zXD47MTAgUklPIFNPTElNT0VTLCBKQVZBUkksSVRBQ1VBSTsxMSBSSU8gU09MSU1PRVMsSUNBLEpBTkRJQVRVQkEsLi47MTIgUklPIFNPTElNT0VTLEpVUlVBLEpBUFVSQSwuLi4uOzEzIFJJTyBTT0xJTU9FUyxQVVJVUyxDT0FSSSwuLjsxNCBSSU8gU09MSU1PRVMsTkVHUk8sQlJBTkNPLC4uLi47MTUgUklPIEFNQVpPTkFTLE1BREVJUkEsR1VBUE9SRSwuOzE2IFJJTyBBTUFaT05BUyxUUk9NQkVUQVMsT1VUUk9TOzE3IFJJTyBBTUFaT05BUyxUQVBBSk9TLEpVUlVFTkEuLjsxOCBSSU8gQU1BWk9OQVMsWElOR1UsSVJJUkksUEFSVTsxOSBSSU8gQU1BWk9OQVMsSkFSSSxQQVJBLE9VVFJPUzsyMCBSSU8gVE9DQU5USU5TLE1BUkFOSEFPLEFMTUFTLi47MjEgUklPIFRPQ0FOVElOUyxQQVJBTkEsUEFMTUEuLi4uOzIyIFJJTyBUT0NBTlRJTlMsTS4gQUxWRVMsU09OTy4uLjsyMyBSSU8gVE9DQU5USU5TLE0uIEFMVkVTIEdSQU5ERTsyNCBSSU8gQVJBR1VBSUEsQ0FJQVBPLENMQVJPLi4uLi47MjUgUklPIEFSQUdVQUlBLENSSVhBUy1BQ1UsUEVJWEU7MjYgUklPIEFSQUdVQUlBLE1PUlRFUyxKQVZBRVMuLi4uOzI3IFJJTyBBUkFHVUFJQSxDT0NPLFBBVSBEJ0FSQ08uLjsyOCBSSU8gQVJBR1VBSUEsTVVSSUNJWkFMLExPTlRSQTsyOSBSSU8gVE9DQU5USU5TLElUQUNBSVVOQVMsLi4uLi47MzAgUklPUyBPSUFQT1FVRSxBUkFHVUFSSSBFIC4uLi4uOzMxIFJJT1MgTUVSVVUsQUNBUkEsR1VBTUEsIE9VVFJPUzszMiBSSU9TIEdVUlVQSSxUVVJJQUNVIEUgT1VUUk9TOzMzIFJJT1MgTUVBUklNLElUQVBFQ1VSVSBFIE9VVFJPUzszNCBSSU8gUEFSTkFJQkE7MzUgUklPUyBBQ0FSQVUsUElSQU5KSSBFIE9VVFJPUzszNiBSSU8gSkFHVUFSSUJFOzM3IFJJT1MgQVBPREksUElSQU5IQVMgRSBPVVRST1M7MzggUklPUyBQQVJBSUJBLFBPVEVOSkkgRSBPVVRST1M7MzkgUklPUyBDQVBJQkFSSUJFLE1VTkRBVSBFIC4uLi4uOzQwIFJJT1MgU0FPIEZSQU5DSVNDTyxQQVJBT1BFQkEgRTs0MSBSSU9TIFNBTyBGUkFOQ0lTQ08sREFTIFZFTEhBUzs0MiBSSU9TIFNBTyBGUkFOQ0lTQ08sUEFSQUNBVFUgRTs0MyBSSU9TIFNBTyBGUkFOQ0lTQ08sVVJVQ1VJQSBFIC47NDQgUklPUyBTQU8gRlJBTkNJU0NPLFZFUkRFIEdSRDs0NSBSSU9TIFNBTyBGUkFOQ0lTQ08sQ0FSSU5IQU5IQTs0NiBSSU9TIFNBTyBGUkFOQ0lTQ08sR1JBTkRFIEUgLi47NDcgUklPUyBTQU8gRlJBTkNJU0NPLEpBQ0FSRSBFIC4uOzQ4IFJJT1MgU0FPIEZSQU5DSVNDTyxQQUpFVSBFIC4uLjs0OSBSSU9TIFNBTyBGUkFOQ0lTQ08sTU9YT1RPIEUgLi47NTAgUklPUyBWQVpBLUJBUlJJUyxJVEFQSUNVUlUgRSAuOzUxIFJJT1MgUEFSQUdVQUNVLEpFUVVJUklDQSBFIC4uLjs1MiBSSU8gREUgQ09OVEFTOzUzIFJJT1MgUEFSRE8sQ0FDSE9FSVJBIEUgT1VUUk9TOzU0IFJJTyBKRVFVSVRJTkhPTkhBOzU1IFJJT1MgTVVDVVJJLFNBTyBNQVRFVVMgRSAuLi4uOzU2IFJJTyBET0NFOzU3IFJJT1MgSVRBUEVNSVJJTSxJVEFCQVBPQU5BIEUgLjs1OCBSSU8gUEFSQUlCQSBETyBTVUw7NTkgUklPUyBNQUNBRSxTQU8gSk9BTyBFIE9VVFJPUzs2MCBSSU8gUEFSQU5BSUJBOzYxIFJJTyBHUkFOREU7NjIgUklPUyBQQVJBTkEsVElFVEUgRSBPVVRST1M7NjMgUklPUyBQQVJBTkEsUEFSRE8gRSBPVVRST1M7NjQgUklPUyBQQVJBTkEsUEFSQU5BUEFORU1BIEUgLi4uOzY1IFJJT1MgUEFSQU5BLElHVUFDVSBFIE9VVFJPUzs2NiBSSU9TIFBBUkFHVUFJLFNBTyBMT1VSRU5DTyBFIC47NjcgUklPUyBQQVJBR1VBSSxBUEEgRSBPVVRST1M7NjggUklPUyBQQVJBTkEsQ09SUklFTlRFUyBFIC4uLi4uOzY5IFJJT1MgUEFSQU5BLFRFUkNFUk8gRSBPVVRST1M7NzAgUklPIFBFTE9UQVM7NzEgUklPIENBTk9BUzs3MiBSSU9TIFVSVUdVQUksRE8gUEVJWEUgRSBPVVRST1M7NzMgUklPUyBVUlVHVUFJLENIQVBFQ08gRSBPVVRST1M7NzQgUklPUyBVUlVHVUFJLERBIFZBUlpFQSBFIC4uLi47NzUgUklPUyBVUlVHVUFJLElKVUkgRSBPVVRST1M7NzYgUklPUyBVUlVHVUFJLElCSUNVSSBFIE9VVFJPUzs3NyBSSU9TIFVSVUdVQUksUVVBUkFJIEUgT1VUUk9TOzc4IFJJT1MgVVJVR1VBSSBFIE9VVFJPUzs3OSBSSU8gVVJVR1VBSSwgTkVHUk8gRSBPVVRST1M7ODAgUklPUyBJVEFQQU5IQVUsSVRBTkhBRU0gRSAuLi4uOzgxIFJJTyBSSUJFSVJBIERPIElHVUFQRTs4MiBSSU9TIE5IVU5ESUFRVUFSQSxJVEFQT0NVIEUgLi47ODMgUklPIElUQUpBSS1BQ1U7ODQgUklPUyBUVUJBUkFPLEFSQVJBTkdVQSBFIC4uLi4uOzg1IFJJTyBKQUNVSTs4NiBSSU8gVEFRVUFSSTs4NyBMQUdPQSBET1MgUEFUT1M7ODggTEFHT0EgTUlSSU07OTAgT1VUUk9TIFJJT1M7PjtAPDA7MTA7MTE7MTI7MTM7MTQ7MTU7MTY7MTc7MTg7MTk7MjA7MjE7MjI7MjM7MjQ7MjU7MjY7Mjc7Mjg7Mjk7MzA7MzE7MzI7MzM7MzQ7MzU7MzY7Mzc7Mzg7Mzk7NDA7NDE7NDI7NDM7NDQ7NDU7NDY7NDc7NDg7NDk7NTA7NTE7NTI7NTM7NTQ7NTU7NTY7NTc7NTg7NTk7NjA7NjE7NjI7NjM7NjQ7NjU7NjY7Njc7Njg7Njk7NzA7NzE7NzI7NzM7NzQ7NzU7NzY7Nzc7Nzg7Nzk7ODA7ODE7ODI7ODM7ODQ7ODU7ODY7ODc7ODg7OTA7Pj47bDxpPDA%2BOz4%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w8MTQ5OTAwMDA7Pj47Pjs7Pjs%2BPjs%2BPjtsPGxzdEVzdGFjYW87YnRBdHVhbGl6YXI7bHN0RGlzcG9uaXZlbDtsc3RPcmlnZW07bHN0QmFjaWE7bHN0U3ViQmFjaWE7Pj7qWEOqVYtT2GXSnbZD16OBTevDZw==&lstBacia=0&lstDisponivel=2&lstEstacao=30659600&lstOrigem=5&lstSubBacia=0&txtAnofim=${year}&txtAnoini=${yearini}&txtCodigo=14990000&txtDiafim=${day}&txtDiaini=${dayini}&txtMesfim=${month}&txtMesini=${monthini}" "http://mapas-hidro.ana.gov.br/Usuario/Exportar.aspx"
