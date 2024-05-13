
# Bank Application

Là một dự án Java với chủ đề là Bank Application phân cấp Admin - Client. Có sử dụng thư viện chủ đạo là JavaFx. Với phần UI được thiết kế thông qua Scene Builder và phần backend được xử lý thông qua mô hình MVC 

## Authors

- [@JohnWickCP](https://github.com/JohnWickCP)

## Hướng dẫn cài đặt JavaFX SDK và thư viện

### Tải JavaFX SDK 22

1. Trước tiên, hãy [tải JavaFX SDK phiên bản 22](https://gluonhq.com/products/javafx/) từ trang web chính thức của Gluon.

2. Giải nén tệp tải về vào một vị trí thuận tiện trên máy tính của bạn.

### Cài đặt thư viện cho IDE 
Sau khi tải dự án tự gitHub về
#### Đói với IDE InteliJ
##### JavaFX
1. Click chuột phải vào dự án -> Open Module Settings -> Add(+) -> Chọn 'Java' -> vào thư mục JavaFX22 mà bạn đã tải-> Chọn 'Lib' -> Chọn hết toàn bộ file -> Đặt tên là Javafx22 -> chuột phải chọn 'Add to Module'

2. Chọn SDKs - > Add -> Chọn thư mục JavaFX22 bạn đã tải -> ok

3. Ở Project, ở phần SDK Chọn Javafx22

4. Nhấp 'Apply' -> Ok

##### FontawesomeFx và SQLite JDBC

1. Thư viện FontawesomeFx và SQLite JDBC đã có sẵn trong dự án và bây giờ bạn chỉ cần cài đặt nó

2. Thêm thư viện vào dự án của bạn bằng cách thực hiện các bước sau:
   - Trong IDE của bạn, mở cài đặt dự án hoặc tùy chọn tương tự.
   - Tìm phần quản lý thư viện hoặc dependencies.
   - Thêm FontawesomeFX và SQLite JDBC vào danh sách các thư viện, 2 file .jar nằm tưng ứng trong folder Icon và DatabaseLib


### Đối với IDE Eclipse

#### JavaFX
- Trong Eclipse, chuột phải vào tên dự án trong thanh chọn lựa bên trái (Project Explorer).
- Chọn Properties.
- Tại cửa sổ Properties, chọn Java Build Path từ menu bên trái.
- Trong tab Libraries, nhấn Add Library....
- Chọn User Library và nhấn Next.
- Nhấn User Libraries... và New....
- Đặt tên cho thư viện (ví dụ: JavaFX22) và nhấn OK.
- Chọn thư viện mới tạo và nhấn Add External JARs....
- Chọn tất cả các tệp .jar trong thư mục lib của thư mục JavaFX SDK 22.
- Nhấn OK để xác nhận và đóng cửa sổ.

#### FontawesomeFx và DatabaseLib 
- Chuột phải vào tên dự án trong Eclipse.
- Chọn Properties.
- Tại cửa sổ Properties, chọn Java Build Path từ menu bên trái.
- Trong tab Libraries, chọn thư viện JavaFX22 (hoặc tên bạn đã chọn) và nhấn Edit....
- Nhấn Add External JARs... và chọn tệp .jar của FontawesomeFX và SQLite JDBC từ thư mục dự án của bạn.
- Nhấn OK để xác nhận và đóng cửa sổ.
