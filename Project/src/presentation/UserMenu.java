package presentation;

import business.config.IOFile;
import business.config.InputMethods;
import business.design.MovieDesign;
import business.design.UserDesign;
import business.implement.MovieImplement;
import business.implement.UserImplement;

public class UserMenu {
    public static void Menu() {
        boolean isExit = false;
        MovieDesign myMovie = new MovieImplement();
        UserDesign myAccount = new UserImplement();
        while (!isExit) {
            System.out.println("============================USER MENU============================");
            System.out.println("----------------------------Trang chủ--------------------------");
            System.out.println("1. Tìm kiếm phim");
            System.out.println("2. Hiển thị tất cả phim hiện có");
            System.out.println("3. Hiển thị danh sách phim mới");
            System.out.println("----------------------Trang danh sách phim----------------------");
            System.out.println("4. Danh sách phim");
            System.out.println("5. Hiển thị phim theo danh mục");
            System.out.println("6. Sắp xếp phim theo tên");
            System.out.println("----------------------Chi tiết phim----------------------");
            System.out.println("7. Hiển thị dữ liệu chi tiết");
            System.out.println("8. Xem phim");
            System.out.println("--------------------------Danh sách yêu thích----------------------------");
            System.out.println("9. Hiển thị danh sách yêu thích");
            System.out.println("10. Xóa phim khỏi danh sách yêu thích");
            System.out.println("--------------------------Thông tin cá nhân--------------------------");
            System.out.println("11. Hiển thị thông tin cá nhân");
            System.out.println("12. Đổi mật khẩu");
            System.out.println("13. Chỉnh sửa thông tin cá nhân");
            System.out.println("--------------------------Lịch sử xem phim---------------------------------");
            System.out.println("14. Hiển thị lịch sử");
            System.out.println("15. Đăng xuất");
            System.out.println("Nhập lựa chọn");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    myMovie.searchByName();
                    break;
                case 2:
                    myMovie.displayAll();
                    break;
                case 3:
                    myMovie.displayNewestMovie();
                    break;
                case 4:
                    myMovie.displayListMovie();
                    break;
                case 5:
                    myMovie.displayMovieByCategory();
                    break;
                case 6:
                    myMovie.sortMovieByName();
                    break;
                case 7:
                    myMovie.displayMovie();
                    break;
                case 8:
                    myMovie.watchMovie();
                    break;
                case 9:
                    myAccount.displayFavoriteList();
                    break;
                case 10:
                    myAccount.removeFromFavoriteList();
                    break;
                case 11:
                    myAccount.displayInformation();
                    break;
                case 12:
                    myAccount.changePassword();
                    break;
                case 13:
                    myAccount.updateInformation();
                    break;
                case 14:
                    myAccount.readHistory();
                    break;
                case 15:
                    Login.user=null;
                    IOFile.updateUserLogin(Login.user);
                    isExit = true;
                    break;
                default:
                    System.err.println("Mời nhập lại");
                    break;
            }
        }
    }
}
