package business.implement;

import business.config.IOFile;
import business.config.InputMethods;
import business.design.UserDesign;
import business.entity.Favorite;
import business.entity.History;
import business.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import presentation.Login;


import java.time.LocalDateTime;
import java.util.List;

import static business.implement.AuthenticationImplement.userList;
import static business.implement.FavoriteImplement.favoriteList;
import static business.implement.HistoryImplement.historyList;
import static business.implement.MovieImplement.moviesList;

public class UserImplement implements UserDesign {
    @Override
    public void displayAll() {
        userList.forEach(User::displayData);
    }

    @Override
    public void createUser() {
        System.out.println("Nhập số lượng người dùng muốn thêm");
        int count = InputMethods.getInteger();
        for (int i = 0; i < count; i++) {
            User newUser = new User();
            System.out.println("Nhập thông tin cho người dùng thứ " + (i + 1));
            newUser.inputData();
            userList.add(newUser);
            IOFile.updateFile(IOFile.USER_PATH, userList);
        }
    }

    @Override
    public void searchUser() {
        User findUser = findUserById();
        if (findUser != null) {
            findUser.displayData();
        } else {
            System.err.println("Người dùng không tồn tại");
        }
    }

    private static User findUserById() {
        System.out.println("Nhập ID người dùng cần tìm:");
        int userId = InputMethods.getInteger();
        return userList.stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    @Override
    public void changeStatusUser() {
        User user = findUserById();
        if (user != null) {
            user.setStatus(!user.isStatus());
            user.setUpdatedAt(LocalDateTime.now());
            System.out.println("Thay đổi thành công");
        } else {
            System.err.println("Người dùng không tồn tại");
        }
        IOFile.updateFile(IOFile.USER_PATH, userList);
    }

    @Override
    public void displayFavoriteList() {
        Favorite myFavorite = favoriteList.stream().filter(favorite -> favorite.getUserId() == Login.user.getUserId()).findFirst().orElse(null);
        if (myFavorite != null) {
            myFavorite.displayData();
        } else {
            System.err.println("Danh sách yêu thích của bạn trống, hãy thêm phim yêu thích của mình trước");

        }
    }

    @Override
    public void removeFromFavoriteList() {
        Favorite myFavorite = favoriteList.stream().filter(favorite -> favorite.getUserId() == Login.user.getUserId()).findFirst().orElse(null);
        if (myFavorite != null) {
            System.out.println("Nhập ID phim muốn xóa khỏi danh sách yêu thích");
            int movieId = InputMethods.getInteger();
            List<Integer> listMovieID = myFavorite.getMovieId();
            if (listMovieID.stream().anyMatch(myMovie -> myMovie == movieId)) {
                listMovieID.remove(listMovieID.indexOf(movieId));
                myFavorite.setMovieId(listMovieID);
            }
        } else {
            System.err.println("Danh sách yêu thích của bạn trống");
        }
        IOFile.updateFile(IOFile.FAVORITE_PATH, favoriteList);
    }

    @Override
    public void displayInformation() {
        Login.user.displayData();
    }

    @Override
    public void changePassword() {
        User user = Login.user;
        while (true) {
            System.out.println("Nhập mật khẩu hiện tại: ");
            String password = InputMethods.getString();
            if (BCrypt.checkpw(password, user.getPassword())) {
                user.inputPassword();
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5)));
//                userList.set(userList.indexOf(user),user );
                break;
            } else {
                System.err.println("Mật khẩu hiện tại không chính xác");
            }
        }
        Login.user.setUpdatedAt(LocalDateTime.now());
        IOFile.updateFile(IOFile.USER_PATH, userList);
    }

    @Override
    public void updateInformation() {
        User user = Login.user;
        boolean isExit = false;
        while (!isExit) {
            System.out.println("Chọn thông tin muốn thay đổi");
            System.out.println("1. Thay đổi họ và tên");
            System.out.println("2. Thay đổi email");
            System.out.println("3. Thay đổi mật khẩu");
            System.out.println("4. Thay đổi hình đại diện");
            System.out.println("5.Thoát");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    System.out.println("Tên cũ: " + user.getFullName());
                    user.inputFullname();
                    break;
                case 2:
                    System.out.println("Email cũ: " + user.getEmail());
                    user.setEmail("");
                    user.inputEmail();
                    break;
                case 3:
                    while (true) {
                        System.out.println("Nhập mật khẩu hiện tại: ");
                        String password = InputMethods.getString();
                        if (BCrypt.checkpw(password, user.getPassword())) {
                            user.inputPassword();
                            user.inputPassword();
                            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5)));
                            break;
                        } else {
                            System.err.println("Mật khẩu hiện tại không chính xác");
                        }
                    }
                    break;
                case 4:
                    System.out.println("Avatar cũ: " + user.getAvatar());
                    user.inputAvatar();
                    break;
                case 5:
                    isExit = true;
                    break;
                default:
                    break;
            }
        }
        IOFile.updateFile(IOFile.USER_PATH, userList);
        System.out.println("Thay đổi thành công");
    }

    public void getNewMovieAndNewUserInMonth() {
        System.out.println("Chọn tháng muốn lấy thống kê");
        byte month = InputMethods.getByte();
        int countMovie = (int) moviesList.stream().filter(movies -> movies.getCreatedDate().getMonthValue() == month).count();
        int countUser = (int) userList.stream().filter(user -> user.getCreatedAt().getMonthValue() == month).count();
        System.out.println("Số lượng phim mới trong tháng " + month + " là: " + countMovie);
        System.out.println("Số lượng người dung mới trong tháng " + month + " là: " + countUser);
    }

    @Override
    public void readHistory() {
       History myHistory = historyList.stream().filter(history -> history.getUserId()==Login.user.getUserId()).findFirst().orElse(null);
       if (myHistory!=null){
           myHistory.displayData();
       } else {
           System.err.println("Lịch sử xem phim của bạn trống");
       }
    }
}
