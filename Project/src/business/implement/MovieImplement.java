package business.implement;

import business.config.IOFile;
import business.config.InputMethods;
import business.design.MovieDesign;
import business.entity.*;
import presentation.Login;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static business.implement.AuthenticationImplement.userList;
import static business.implement.FavoriteImplement.favoriteList;
import static business.implement.HistoryImplement.historyList;
import static business.implement.RateImplement.rateList;

public class MovieImplement implements MovieDesign<Movies> {
    public static List<Movies> moviesList;

    static {
        File moviesFile = new File(IOFile.MOVIES_PATH);
        if (moviesFile.length() == 0) {
            moviesList = new ArrayList<>();
            IOFile.updateFile(IOFile.MOVIES_PATH, moviesList);
        } else {
            moviesList = IOFile.getFile(IOFile.MOVIES_PATH);
        }
    }

    @Override
    public void searchByName() {
        System.out.println("Nhập tên phim cần tìm:");
        String movieName = InputMethods.getString();
        Movies movies = moviesList.stream().filter(movies1 -> movies1.getMovieName().equals(movieName)).findFirst().orElse(null);
        if (movies != null) {
            System.out.println("Kết quả:");
            System.out.println("Tên phim: " + movies.getMovieName());
            System.out.println("Thuộc danh mục: " + movies.getCategoryName());
            System.out.println("Mô tả: " + movies.getDescription());
            System.out.println("1. Thêm vào danh sách yêu thích");
            System.out.println("2. Thoát");
            System.out.println("Lựa chọn");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    addToFavoriteList(movies);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Mời nhập lại");
            }
        } else {
            System.err.println("Phim không tồn tại");
        }
    }

    @Override
    public void displayNewestMovie() {
        List<Movies> listNewestMovie = moviesList.stream().sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate())).toList();
        if (!listNewestMovie.isEmpty() && listNewestMovie.size() <= 3) {
            listNewestMovie.forEach(Movies::displayData);
        } else if (listNewestMovie.size() > 3){
            for (int i = 0; i < 3; i++) {
                listNewestMovie.get(i).displayData();
            }
        } else {
            System.err.println("Danh sách phim trống");
        }
    }

    @Override
    public void displayMovieByCategory() {
        System.out.println("Nhập tên danh mục phim cần hiển thị:");
        String categoryName = InputMethods.getString();
        List<Movies> movies = moviesList.stream().filter(movies1 -> movies1.getCategoryName().equals(categoryName)).toList();
        if (movies.isEmpty()) {
            System.err.println("Danh mục không có phim nào.");
        } else {
            movies.forEach(Movies::displayData);
        }
    }

    @Override
    public void sortMovieByName() {
        moviesList.sort((o1, o2) -> o1.getMovieName().compareTo(o2.getMovieName()));
        IOFile.updateFile(IOFile.MOVIES_PATH, moviesList);
        displayAll();
    }

    @Override
    public void displayMovie() {
        System.out.println("Nhập tên phim cần tìm:");
        String movieName = InputMethods.getString();
        Movies movies = moviesList.stream().filter(movies1 -> movies1.getMovieName().equals(movieName)).findFirst().orElse(null);
        if (movies != null) {
            System.out.println("Kết quả:");
            movies.displayData();
        } else {
            System.err.println("Phim không tồn tại");
        }
    }

    @Override
    public void addToFavoriteList(Movies movies) {
        Favorite myFavorite = favoriteList.stream().filter(favorite -> favorite.getUserId() == Login.user.getUserId()).findFirst().orElse(null);
        if (myFavorite != null) {
            List<Integer> listMovieId = myFavorite.getMovieId();
            listMovieId.add(movies.getMovieId());
            myFavorite.setMovieId(listMovieId);
        } else {
            myFavorite = new Favorite();
            myFavorite.setMovieId(new ArrayList<>());
            myFavorite.setUserId(Login.user.getUserId());
            List<Integer> listMovieId = myFavorite.getMovieId();
            listMovieId.add(movies.getMovieId());
            myFavorite.setMovieId(listMovieId);
            favoriteList.add(myFavorite);
        }
        IOFile.updateFile(IOFile.FAVORITE_PATH, favoriteList);
    }

    @Override
    public void watchMovie() {
        boolean isExit = false;
        while (!isExit) {
            System.out.println("1. Hiển thị danh sách phim");
            System.out.println("2. Tìm kiếm phim");
            System.out.println("3. Thoát");
            System.out.println("Nhập lựa chọn");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    moviesList.forEach(movies -> System.out.println((moviesList.indexOf(movies) + 1) + ". " + movies.getMovieName()));
                    while (true) {
                        System.out.println("Chọn phim muốn xem: ");
                        int choiceMovie = InputMethods.getInteger();
                        if (choiceMovie >= 1 && choiceMovie <= moviesList.size()) {
                            if (watchByList(choiceMovie)) {
                                addToHistoryAndIncreaseView(choiceMovie);
                                System.out.println("1. Để lại đánh giá của bạn\n" +
                                        "2. Thoát\n" +
                                        "Nhập lựa chọn");
                                byte commentOrNot = InputMethods.getByte();
                                switch (commentOrNot) {
                                    case 1:
                                        leaveComment(moviesList.get(choiceMovie - 1));
                                        break;
                                    case 2:
                                        return;
                                    default:
                                        System.out.println("Mời nhập lại");
                                        break;
                                }
                            }
                        } else {
                            System.err.println("Sô thứ tự nhập vào không chính xác, mời nhập lại");
                        }
                    }
                case 2:
                    System.out.println("Nhập tên phim muốn xem:");
                    String movieName = InputMethods.getString();
                    if (moviesList.stream().anyMatch(movies -> movies.getMovieName().equals(movieName))) {
                        int indexMovie = moviesList.indexOf(moviesList.stream().filter(movies -> movies.getMovieName().equals(movieName)).findFirst().orElse(null));
                        if (watchByList(indexMovie + 1)) {
                            addToHistoryAndIncreaseView(indexMovie + 1);

                            System.out.println("1. Để lại đánh giá của bạn\n" +
                                    "2. Thoát\n" +
                                    "Nhập lựa chọn");
                            byte commentOrNot = InputMethods.getByte();
                            switch (commentOrNot) {
                                case 1:
                                    leaveComment(moviesList.get(indexMovie));
                                    break;
                                case 2:
                                    return;
                                default:
                                    System.out.println("Mời nhập lại");
                                    break;
                            }
                            return;
                        }
                    } else {
                        System.err.println("Phim không tồn tại");
                    }
                    break;
                case 3:
                    isExit = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void leaveComment(Movies movies) {
        System.out.println("Nhập đánh giá của bạn(1-5): ");
        byte rate = InputMethods.getByte();
        System.out.println("Nhập bình luận");
        String comment = InputMethods.getString();
        Rate newRate = new Rate();
        newRate.setUserId(Login.user.getUserId());
        newRate.setMovieId(movies.getMovieId());
        newRate.setRate(rate);
        newRate.setComment(comment);
        rateList.add(newRate);
        IOFile.updateFile(IOFile.RATE_PATH, rateList);
    }

    private static void addToHistoryAndIncreaseView(int choiceMovie) {
        moviesList.get(choiceMovie - 1).setView(moviesList.get(choiceMovie - 1).getView() + 1);
        History myHistory = historyList.stream().filter(history -> history.getUserId() == Login.user.getUserId()).findFirst().orElse(null);
        if (myHistory != null) {
            List<Movies> historyMovie = myHistory.getMovies();
            historyMovie.add(moviesList.get(choiceMovie - 1));
            myHistory.setMovies(historyMovie);
            myHistory.setUpdateDate(LocalDateTime.now());
        } else {
            History newHistory = new History();
            newHistory.setUserId(Login.user.getUserId());
            List<Movies> history = new ArrayList<>();
            history.add(moviesList.get(choiceMovie - 1));
            newHistory.setMovies(history);
            myHistory = newHistory;
            historyList.add(myHistory);
        }
        IOFile.updateFile(IOFile.HISTORY_PATH, historyList);
    }

    private static boolean watchByList(int choiceMovie) {
        System.out.println("Đang xem phim: " + moviesList.get(choiceMovie - 1).getMovieName());
        System.out.println("1.Tạm dừng");
        System.out.println("2.Dừng xem");
        System.out.println("Lựa chọn.");
        byte choiceWatch = InputMethods.getByte();
        switch (choiceWatch) {
            case 1:
                System.out.println("Đang tạm dừng");
                System.out.println("1. Xem tiếp");
                System.out.println("2. Dừng xem");
                System.out.println("Lựa chọn: ");
                byte choiceContinue = InputMethods.getByte();
                switch (choiceContinue) {
                    case 1:
                        watchByList(choiceMovie);
                        break;
                    case 2:
                        return true;
                }
                break;
            case 2:
                return true;
            default:
                System.out.println("Mời nhập lại");
                break;
        }
        return false;
    }

    @Override
    public Movies findById() {
        System.out.println("Nhập ID phim cần tìm:");
        int movieId = InputMethods.getInteger();
        return moviesList.stream().filter(movies -> movies.getMovieId() == movieId).findFirst().orElse(null);
    }

    @Override
    public void createData() {
        System.out.println("Nhập số lượng phim muốn thêm: ");
        int count = InputMethods.getInteger();
        for (int i = 0; i < count; i++) {
            Movies newMovie = new Movies();
            System.out.println("Nhập thông tin cho phim thứ: " + (i + 1));
            newMovie.inputData();
            moviesList.add(newMovie);
            IOFile.updateFile(IOFile.MOVIES_PATH, moviesList);
        }
    }

    @Override
    public void displayAll() {
        moviesList.forEach(Movies::displayData);
        if (moviesList.isEmpty()) {
            System.err.println("Danh sách phim trống");
        }
    }

    @Override
    public void updateData() {
        Movies movies = findById();
        boolean isExit = false;
        while (!isExit) {
            System.out.println("Nhập thông tin muốn cập nhật");
            System.out.println("1. Thay đổi tên phim");
            System.out.println("2. Thay đổi mô tả");
            System.out.println("3. Thay đổi url video");
            System.out.println("4. Thay đổi url image");
            System.out.println("5. Thoát");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    System.out.println("Tên phim cũ: " + movies.getMovieName());
                    movies.setMovieName("");
                    movies.inputMovieName();
                    break;
                case 2:
                    System.out.println("Mô tả cũ:");
                    System.out.println(movies.getDescription());
                    movies.inputMovieDescription();
                    break;
                case 3:
                    System.out.println("Url video cũ:");
                    System.out.println(movies.getVideoUrl());
                    movies.inputMovieVideoUrl();
                    break;
                case 4:
                    System.out.println("Url image cũ:");
                    System.out.println(movies.getImageUrl());
                    movies.inputMovieImageUrl();
                    break;
                case 5:
                    isExit = true;
                    break;
                default:
                    System.out.println("Mời nhập lại");
                    break;
            }
        }
        System.out.println("Sửa thành công");
        movies.setUpdateDate(LocalDateTime.now());
        IOFile.updateFile(IOFile.MOVIES_PATH, moviesList);
    }

    @Override
    public void deleteData() {
        Movies movies = findById();
        if (movies != null) {
            moviesList.remove(movies);
            movies.setUpdateDate(LocalDateTime.now());
            IOFile.updateFile(IOFile.MOVIES_PATH, moviesList);
            System.out.println("Xóa thành công");
        } else {
            System.err.println("Phim không tồn tài");
        }
    }

    public void getTop10View() {
        System.out.println("Nhập tháng muốn nhận thống kê");
        byte month = InputMethods.getByte();
        if (month >= 1 && month <= 12) {
            List<Movies> movies = moviesList.stream().filter(movies1 -> movies1.getCreatedDate().getMonthValue() == month).sorted((o1, o2) -> o2.getView() - o1.getView()).toList();
            if (movies.size() <= 10 && !movies.isEmpty()) {
                movies.forEach(Movies::displayData);
            } else if (movies.size() > 10) {
                for (int i = 0; i < 10; i++) {
                    movies.get(i).displayData();
                }
            } else {
                System.err.println("Danh sách phim trống");
            }
        }
    }

    @Override
    public void displayListMovie() {
        if (moviesList.size() > 0) {
            System.out.println("Danh sách phim hiện có");
            for (int i = 0; i < moviesList.size(); i++) {
                System.out.println((i + 1) + ". " + moviesList.get(i).getMovieName());
            }
        } else {
            System.err.println("Danh sách phim trống");
        }
    }
}
