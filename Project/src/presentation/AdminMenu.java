package presentation;

import business.config.IOFile;
import business.config.InputMethods;
import business.design.CategoryDesign;
import business.design.MovieDesign;
import business.design.UserDesign;
import business.entity.Category;
import business.entity.Movies;
import business.implement.CategoryImplement;
import business.implement.MovieImplement;
import business.implement.UserImplement;

public class AdminMenu {
    public static void Menu() {
        UserDesign useManager = new UserImplement();
        MovieDesign<Movies> movieManager = new MovieImplement();
        CategoryDesign<Category> categoryManager = new CategoryImplement();
        boolean isExit = false;
        while (!isExit) {
            System.out.println("============================ADMIN MENU============================");
            System.out.println("---------------------------Quản lý User---------------------------");
            System.out.println("1. Hiển thị danh sách");
            System.out.println("2. Thêm mới user");
            System.out.println("3. Tìm kiếm user");
            System.out.println("4. Đổi trạng thái user");
            System.out.println("------------------------Quản lý danh mục--------------------------");
            System.out.println("5. Hiển thị danh sách danh mục");
            System.out.println("6. Thêm danh mục");
            System.out.println("7. Sửa danh mục");
            System.out.println("8. Xóa danh mục");
            System.out.println("9. Tìm kiếm danh mục");
            System.out.println("--------------------------Quản lý phim----------------------------");
            System.out.println("10. Hiển thị danh sách phim");
            System.out.println("11. Thêm mới phim");
            System.out.println("12. Sửa thông tin phim");
            System.out.println("13. Xóa phim");
            System.out.println("14. Sắp xếp phim theo tên");
            System.out.println("15. Tìm kiếm phim theo tên");
            System.out.println("--------------------------Thống kê---------------------------------");
            System.out.println("16. Thống kê top 10 phim có lượt xem cao nhất trong tháng");
            System.out.println("17. Thống kê số lượng phim mới, người dùng mới trong tháng");
            System.out.println("18.  Đăng xuất");
            System.out.println("Nhập lựa chọn");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    useManager.displayAll();
                    break;
                case 2:
                    useManager.createUser();
                    break;
                case 3:
                    useManager.searchUser();
                    break;
                case 4:
                    useManager.changeStatusUser();
                    break;
                case 5:
                    categoryManager.displayAll();
                    break;
                case 6:
                    categoryManager.createData();
                    break;
                case 7:
                    categoryManager.updateData();
                    break;
                case 8:
                    categoryManager.deleteData();
                    break;
                case 9:
                    categoryManager.searchCategory();
                    break;
                case 10:
                    movieManager.displayAll();
                    break;
                case 11:
                    movieManager.createData();
                    break;
                case 12:
                    movieManager.updateData();
                    break;
                case 13:
                    movieManager.deleteData();
                    break;
                case 14:
                    movieManager.sortMovieByName();
                    break;
                case 15:
                    movieManager.searchByName();
                    break;
                case 16:
                    movieManager.getTop10View();
                    break;
                case 17:
                    useManager.getNewMovieAndNewUserInMonth();
                    break;
                case 18:
                    Login.user=null;
                    IOFile.updateUserLogin(Login.user);
                    isExit = true;
                    break;
                default:
                    System.out.println("Mời nhập lại");
                    break;
            }
        }
    }
}
