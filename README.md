# LocalAI Android Starter

Ứng dụng Android ưu tiên riêng tư, thiết kế cho chat offline với model GGUF trên điện thoại. Project này là **bản starter UI**, chưa phải bản AI inference hoàn chỉnh.

## Trạng thái hiện tại

- [x] Giao diện chat Android (Jetpack Compose)
- [x] Lưu/xóa lịch sử chat trong bộ nhớ riêng ứng dụng
- [x] Chọn tệp từ trình chọn tài liệu Android
- [x] Màn hình thiết lập context, CPU threads, max tokens, temperature
- [x] GitHub Actions build APK và lưu artifact; tag `v*` sẽ đính kèm APK vào Release
- [ ] Native inference llama.cpp: **chưa tích hợp**
- [ ] Nạp model GGUF và sinh token thật: **chưa hoạt động**
- [ ] Kiểm thử trên thiết bị Android thật: **chưa thực hiện**

Không phát hành hay mô tả bản này như một AI chạy được cho đến khi hoàn tất các mục chưa làm.

## Phần cứng khuyến nghị

Với điện thoại 4 GB RAM, bắt đầu bằng model nhỏ khoảng 1B–1.5B ở quantization Q4 và context 512–1024. Đây chỉ là điểm bắt đầu; RAM khả dụng thay đổi theo thiết bị, phiên bản Android và các ứng dụng nền. Không nhúng model vào APK; để người dùng chọn/tải riêng, vì file model có thể lớn hơn nhiều so với ứng dụng.

## Kết nối runtime thật

Dùng implementation Android chính thức của llama.cpp làm nền tảng runtime:
https://github.com/ggml-org/llama.cpp/tree/master/examples/llama.android

Tài liệu upstream:
https://github.com/ggml-org/llama.cpp/blob/master/docs/android.md

Tích hợp cần thay `RuntimeNotConfiguredEngine` bằng adapter gọi binding inference, sao chép hoặc mở model theo yêu cầu API của runtime, rồi kết nối luồng token vào UI. Kiểm tra API và giấy phép của phiên bản upstream mà bạn chọn. Đừng tải tệp model không rõ nguồn gốc; hãy xem giấy phép model trước khi phân phối.

## Build tự động trên GitHub

1. Tạo repository GitHub mới, ví dụ `LocalAI-Android`.
2. Giải nén project, commit và push toàn bộ file lên nhánh `main`.
3. Mở tab **Actions** và bật workflow nếu GitHub yêu cầu.
4. Mở workflow **Build Android APK**, chọn **Run workflow** hoặc push commit mới.
5. Khi job hoàn tất, tải `LocalAI-debug-apk` trong phần Artifacts.
6. Để tạo GitHub Release, tạo tag và push:

```bash
git tag v0.1.0
git push origin v0.1.0
```

Workflow hiện phát hành **debug APK chưa ký release**, phù hợp thử nghiệm. Trước khi phân phối rộng rãi, hãy cấu hình ký APK release bằng GitHub Actions secrets; không commit keystore hoặc mật khẩu vào repository.

## Build thủ công

Cần JDK 17, Android SDK và Gradle 8.13. Từ thư mục gốc:

```bash
gradle assembleDebug
```

APK đầu ra dự kiến:
`app/build/outputs/apk/debug/app-debug.apk`

## Quyền riêng tư

Bản starter không khai báo quyền Internet và không gửi tin nhắn lên máy chủ. Khi thêm thư viện hoặc runtime, hãy kiểm tra lại quyền trong manifest và hành vi mạng của các dependency.

## License

Mã nguồn starter được phát hành theo MIT; các dependency và model có giấy phép riêng.
