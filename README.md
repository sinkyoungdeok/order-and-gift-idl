# order-and-gift-idl
주문,선물 프로젝트의 grpc idl 레포입니다.


# 초기 buf 생성 
```
buf mod init
```

# Init & Lint & Compile
```bash
cd protos
buf mod update
buf lint 
buf build 
buf generate .
```