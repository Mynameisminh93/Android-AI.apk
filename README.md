# LocalAI Android Starter

Ứng dụng Android ưu tiên riêng tư, được chuẩn bị để tích hợp chat offline với model GGUF trên điện thoại.

## Trạng thái hiện tại

- [x] Giao diện chat Android bằng Jetpack Compose
- [x] Chọn tệp qua trình chọn tài liệu Android (bao gồm tệp model GGUF)
- [x] GitHub Actions biên dịch debug APK và lưu artifact
- [ ] Runtime suy luận llama.cpp/NDK: **chưa tích hợp**
- [ ] Nạp model và sinh token thật: **chưa hoạt động**
- [ ] Kiểm thử trên thiết bị Android thật: **chưa thực hiện**

Chọn được tệp model không có nghĩa model đã được nạp hoặc chạy. Bản hiện tại chưa phải trợ lý AI hoạt động; giao diện sẽ thông báo rõ điều này.

## Hướng tích hợp runtime

Có thể dùng llama.cpp Android làm nền tảng tham khảo:
- https://github.com/ggml-org/llama.cpp/tree/master/examples/llama.android
- https://github.com/ggml-org/llama.cpp/blob/master/docs/android.md

Bước tích hợp cần thêm native build/NDK hoặc binding phù hợp, adapter Kotlin cho API inference, quản lý vòng đời model, hủy sinh token và chạy inference ngoài main thread. Cần kiểm tra API và giấy phép của phiên bản upstream được chọn.

## Model và bộ nhớ

Không nhúng model vào APK vì tệp GGUF thường lớn hơn nhiều so với ứng dụng. Người dùng nên chọn model từ nguồn đáng tin cậy và kiểm tra giấy phép. Model nhỏ hơn thường phù hợp hơn trên điện thoại ít RAM; mức dùng được phụ thuộc thiết bị và context.

## Build Android APK

Workflow Build Android APK chạy khi có commit trên nhánh main hoặc khi được chạy thủ công. Sau khi hoàn tất:
1. Mở tab Actions của repository.
2. Chọn lượt chạy có trạng thái thành công.
3. Tải artifact LocalAI-debug-apk.

Đây là debug APK để thử nghiệm, chưa phải bản release ký chính thức.

## Quyền riêng tư

Manifest hiện không khai báo quyền Internet; giao diện starter không gửi tin nhắn đến máy chủ. Khi thêm runtime hoặc thư viện, hãy kiểm tra lại quyền và hành vi mạng của các dependency.

## License

Mã nguồn starter theo MIT. Các dependency và model có giấy phép riêng.
