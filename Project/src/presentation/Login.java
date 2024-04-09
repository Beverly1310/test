package presentation;

import business.config.IOFile;
import business.config.InputMethods;
import business.design.IAuthentication;
import business.entity.User;
import business.implement.AuthenticationImplement;

import java.io.File;

public class Login {
    private static IAuthentication iAuthentication = new AuthenticationImplement();
    public static User user = null;

    static {
        File loginStatus = new File(IOFile.LOGIN_STATUS_PATH);
        if (!(loginStatus.length() == 0)) {
            user = IOFile.getUserLogin();
        }
        if (user!=null){
            {
                if (user.isRole()) {
                    AdminMenu.Menu();
                } else {
                    UserMenu.Menu();
                }
            }
        }
    }

    public static void main(String[] args) {
        boolean isExit = false;
        while (!isExit) {
            System.out.println("=================TRANG CHỦ=================\n" +
                    "1. Đăng nhập\n" +
                    "2. Đăng ký\n" +
                    "3.Thoát\n" +
                    "Mời nhập lựa chọn.");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    if (user == null) {
                        login();
                    } else {
                        if (user.isRole()) {
                        AdminMenu.Menu();
                    } else {
                        UserMenu.Menu();
                    }
                    }
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    isExit = true;
                    break;
                default:
                    System.err.println("Mời nhập lại");
                    break;
            }
        }
    }

    public static void login() {
        System.out.println("=================ĐĂNG NHẬP=================");
        System.out.println("Nhập Username:");
        String username = InputMethods.getString();
        System.out.println("Nhập Password:");
        String password = InputMethods.getString();
        User userLogin = iAuthentication.login(username, password);
        if (userLogin == null) {
            System.err.println("Thông tin đăng nhập không chính xác.");
            System.out.println("1. Đăng nhập lại.");
            System.out.println("2. Bạn chưa có tài khoản? Đăng ký ngay.");
            System.out.println("3. Thoát");
            System.out.println("Nhập lựa chọn");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    login();
                    break;
                case 3:
                    return;
                default:
                    System.err.println("Mời nhập lại");
                    break;
            }
        } else {
            IOFile.updateUserLogin(userLogin);
            if (userLogin.isRole()) {
                user = userLogin;
                AdminMenu.Menu();
            } else {
                if (!userLogin.isStatus()) {
                    System.err.println("Tài khoản của bạn đã bị khóa, vui lòng liện hệ Admin để được hỗ trợ");
                } else {
                    user = userLogin;
                    UserMenu.Menu();
                }
            }
        }
    }

    public static void register() {
        System.out.println("=================ĐĂNG KÝ=================");
        User user = new User();
        user.inputUsername();
        user.inputPassword();
        user.inputEmail();
        user.inputFullname();
        user.inputAvatar();
        user.inputKey();
        iAuthentication.register(user);
        System.out.println("Đăng ký thành công");
    }
}

