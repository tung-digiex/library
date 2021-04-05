CREATE DATABASE QLY_SACH
GO

USE QLY_SACH
GO

--drop DATABASE QLY_SACH 

-- 
-- Bảng nhân viên
-- 


CREATE TABLE nhanvien (
	MaNV varchar(50) NOT NULL primary key,
	MatKhau varchar(50) NOT NULL,
	HoTen nvarchar(100) NOT NULL,
	ChucVu bit NOT NULL default 0
) 
GO
--
-- Dữ liệu bảng nhân viên
-- 

INSERT INTO nhanvien VALUES ('NV01', '123', N'Linh Linh',1);
INSERT INTO nhanvien VALUES ('NV02', '123', N'Hoàng Nguyên',0);
INSERT INTO nhanvien VALUES ('NV03', '123', N'Hoàng Tùng',0);
INSERT INTO nhanvien VALUES ('NV04', '123', N'Bảo Việt',0);
INSERT INTO nhanvien VALUES ('NV05', '123', N'Quốc Thái', 0);

-- --------------------------------------------------------

-- 
-- Bảng loại sách
-- 

CREATE TABLE loaisach(
  MaLoai char(10) NOT NULL,
  TenLoai nvarchar(100)NULL,
  PRIMARY KEY  (MaLoai)
)
GO
-- 
-- Dữ liệu bảng loại sách
-- 
INSERT INTO loaisach VALUES ('L01', N'Văn học')
INSERT INTO loaisach VALUES ('L02', N'Kinh tế - Kinh doanh')
INSERT INTO loaisach VALUES ('L03', N'Sách giáo khoa - Tham khảo')
INSERT INTO loaisach VALUES ('L04', N'Truyện tranh')
INSERT INTO loaisach VALUES ('L05', N'Tâm lý - Kỹ năng')


-- --------------------------------------------------------

-- 
-- Bảng tác giả
-- 

CREATE TABLE tacgia (
	  MaTG int identity(1,1) NOT NULL,
	  TenTG Nvarchar(40) NOT NULL,
	  GioiThieu nvarchar(100) null,
	  PRIMARY KEY  (MaTG)
)
GO

--
-- Dữ liệu bảng tác giả
-- 
INSERT INTO tacgia VALUES ( N'Tố Hữu', N'1920-2002 Huế')
INSERT INTO tacgia VALUES ( N'Nguyễn Du', N'1894-1954 Hà Nội')
INSERT INTO tacgia VALUES ( N'T Harv Eker', N'T. Harv Eker là một là một doanh nhân, diễn giả tài năng')
INSERT INTO tacgia VALUES ( N'Bộ Giáo Dục Và Đào Tạo', N'Bộ Giáo dục và Đào tạo là cơ quan của Chính phủ nước Cộng hòa xã hội chủ nghĩa Việt Nam' )
INSERT INTO tacgia VALUES ( N'Dương Hương', N'sinh ngày 8 tháng 7 năm 1949 Tỉnh Thái Bình' )
INSERT INTO tacgia VALUES ( N'Zig Ziglar', N'1926-2012 Mỹ')
INSERT INTO tacgia VALUES ( N'Nhiều tác giả', N'Tác phẩm do nhiều tác giả biên soạn...' )
INSERT INTO tacgia VALUES ( N'Adam Khoo', N'là một trong 1% số sinh viên dẫn đầu trường và được tuyển chọn vào Chương trình phát triển tài năng' )
INSERT INTO tacgia VALUES ( N'Rosie Nguyễn', N'"Cuộc đời tôi thay đổi là nhờ sách"' )
INSERT INTO tacgia VALUES ( N'Shozo Shibuya', N'Chủ nhân cuốn "Nghệ Thuật Nhìn Người Đoán Tính Cách"' )

-- --------------------------------------------------------

-- Bảng sách
-- 
CREATE TABLE sach (
  MaSach int identity(1,1) NOT NULL,
  TenSach nvarchar(100) NOT NULL,
  MaTG int NOT NULL,
  MaLoai char(10)  NOT NULL,
  SoLuong int not null,
  NamXB char(4)NULL,
  NXB nvarchar(50) NULL,  
  Gia int NOT NULL,
  PRIMARY KEY  (MaSach),
  CONSTRAINT FK_SACH_MATACGIA FOREIGN KEY  (MaTG) REFERENCES tacgia(MaTG) on delete cascade,
  CONSTRAINT FK_SACH_MALOAISACH FOREIGN KEY  (MaLoai) REFERENCES loaisach(MaLoai) on delete cascade  
)
GO


-- 
-- Dữ liệu bảng sách
-- 

select* from sach
INSERT INTO sach VALUES ( N'Truyện Thúy Kiều', 2, 'L01', 50, '2018', N'NXB Văn học', 90000)
INSERT INTO sach VALUES ( N'Kim Túy Tình Từ', 2, 'L01', 50, '2018', N'NXB Văn hóa Văn nghệ', 90000)
INSERT INTO sach VALUES ( N'Tố Hữu - Tác Phẩm Và Lời Bình', 1, 'L01', 50, '2015', N'NXB Văn học', 58000)
INSERT INTO sach VALUES ( N'Bí Mật Tư Duy Triệu Phú', 3, 'L02', 50, '2019', N'NXB Tổng Hợp TPHCM', 92000)
INSERT INTO sach VALUES ( N'Sách Giáo Khoa Bộ Lớp 12 Cơ Bản', 4, 'L03', 100, '2020', N'NXB Giáo Dục Việt Nam', 180000)
INSERT INTO sach VALUES ( N'Sách Giáo Khoa Bộ Lớp 11 Cơ Bản', 4, 'L03', 100, '2020', N'NXB Giáo Dục Việt Nam', 169000)
INSERT INTO sach VALUES ( N'Chinh Phục Đề Thi Vào Lớp 10 - Môn Tiếng Anh ', 5, 'L03', 70, '2018', N'NXB Đại Học Quốc Gia Hà Nội', 139000)
INSERT INTO sach VALUES ( N'Nghệ Thuật Bán Hàng Bậc Cao', 6, 'L02', 50, '2016', N'NXB Tổng Hợp TP.HCM', 168000)
INSERT INTO sach VALUES ( N'Sinh Ra Để Giành Chiến Thắng', 6, 'L02', 50, '2019', N'NXB Đại học Kinh tế Quốc dân', 129000)
INSERT INTO sach VALUES ( N'Thần Đồng Đất Việt 8 - Món Ăn Thượng Phúc', 7, 'L04', 50, '2013', N'NXB Đại Học Sư Phạm', 10000)
INSERT INTO sach VALUES ( N'Thần Đồng Đất Việt 28 - Bí Mật Bong Bóng', 7, 'L04', 50, '2013', N'NXB Đại Học Sư Phạm', 10000)
INSERT INTO sach VALUES ( N'Thần Đồng Đất Việt 55 - Lâu Đài Giữa Trời', 7, 'L04', 50, '2013', N'NXB Đại Học Sư Phạm', 10000)
INSERT INTO sach VALUES ( N'Tôi Tài Giỏi, Bạn Cũng Thế!', 8, 'L05', 50, '2019', N'NXB Phụ Nữ', 118000)
INSERT INTO sach VALUES ( N'Tuổi Trẻ Đáng Giá Bao Nhiêu? ', 9, 'L05', 50, '2018', N'NXB Hội Nhà Văn', 80000)
INSERT INTO sach VALUES ( N'Từ Điển Tâm Lý', 10, 'L05', 50, '2020', N'NXB Thế Giới', 128000)

-- --------------------------------------------------------


-- 
-- Bảng nhật kí nhập sách
-- 

CREATE TABLE nhatkinhapsach (
  STT int identity(1,1)  NOT NULL , 
  MaSach int NOT NULL,
  SoLuong int NOT NULL,
  NgayNhap datetime not null ,
  PRIMARY KEY  (STT),
  CONSTRAINT FK_NHATKINHAPSACH_MASACH FOREIGN KEY  (MaSach) REFERENCES sach(MaSach) on delete cascade
) 
GO
-- 
-- Dữ liệu bảng nhật kí nhập sách
-- 
INSERT INTO nhatkinhapsach VALUES (1, 50, '2020-03-20')
INSERT INTO nhatkinhapsach VALUES (2,50, '2020-02-20')
INSERT INTO nhatkinhapsach VALUES (3, 50, '2020-01-20')
INSERT INTO nhatkinhapsach VALUES (4, 50, '2020-03-20')
INSERT INTO nhatkinhapsach VALUES (5, 50, '2020-04-20')
INSERT INTO nhatkinhapsach VALUES (6, 100, '2020-05-20')
INSERT INTO nhatkinhapsach VALUES (7, 100, '2020-06-20')
INSERT INTO nhatkinhapsach VALUES (8, 70, '2020-06-20')
INSERT INTO nhatkinhapsach VALUES (9, 50, '2020-05-20')
INSERT INTO nhatkinhapsach VALUES (10, 50, '2020-04-20')
INSERT INTO nhatkinhapsach VALUES (11, 50, '2020-03-20')
INSERT INTO nhatkinhapsach VALUES (12, 50, '2020-02-20')
INSERT INTO nhatkinhapsach VALUES (13, 50, '2020-01-20')
INSERT INTO nhatkinhapsach VALUES (14, 50, '2020-02-20')
INSERT INTO nhatkinhapsach VALUES (15, 50, '2020-04-20')


-- --------------------------------------------------------


-- 
-- Bảng hóa đơn
-- 
CREATE TABLE hoadon(
  MaHD int identity(1,1) NOT NULL,
  TenKH nvarchar(50) NULL,
  MaNV varchar(50) NOT NULL,
  PRIMARY KEY  (MaHD),  
  CONSTRAINT FK_HOADON_NHANVIEN FOREIGN KEY  (MaNV) REFERENCES nhanvien(MaNV) on delete cascade  
)
GO


insert into hoadon values
('viet','NV01' ),
('thai','NV01')

GO


-- --------------------------------------------------------
-- 
-- Bảng chi tiết hóa đơn
-- 

CREATE TABLE chitiethoadon (
  MaHD int  NOT NULL,  
  MaSach  int NOT NULL,
  SoLuong int NOT NULL,
  Gia int NULL,  
  ThanhTien int not null,
  NgayLap datetime NULL,
  primary key (MaHD,MaSach),
  CONSTRAINT FK_CHITIETHOADON_HOADON FOREIGN KEY  (MaHD) REFERENCES hoadon(MaHD) on delete cascade,
  CONSTRAINT FK_CHITIETHOADON_MASACH FOREIGN KEY  (MaSach) REFERENCES sach(MaSach) on delete cascade 
)



INSERT into chitiethoadon VALUES
(1,2,2,90000,180000,'2020-7-7'),
(2,6,2,90000,180000,'2020-7-7'),
(2,1,2,90000,180000,'2020-7-7')



IF OBJECT_ID ('[sp_ThongKeDoanhThu]') IS NOT NULL
	DROP PROC [sp_ThongKeDoanhThu]
GO

CREATE PROC [dbo].[sp_ThongKeDoanhThu](@Year INT)
AS BEGIN
	SELECT
		s.TenSach TenSach,
		hd.MaSach MaSach,
		c.MaHD MaHD,
		SUM(hd.SoLuong) SoLuong,
		SUM(hd.ThanhTien) DoanhThu
	FROM sach s
		JOIN chitiethoadon hd ON hd.MaSach=s.MaSach
		JOIN hoadon c on hd.MaHD= c.MaHD
	WHERE YEAR(NgayLap) = @Year
	GROUP BY s.TenSach, hd.MaSach, c.MaHD
END
