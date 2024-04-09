package business.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import static business.implement.AuthenticationImplement.userList;
import static business.implement.FavoriteImplement.favoriteList;

public class Favorite implements Serializable {
    private int favoriteId;
    private int userId;
    private List<Integer> movieId;

    public Favorite() {
        this.favoriteId = getNewId();
    }

    public Favorite(int favoriteId, int userId, List<Integer> movieId) {
        this.favoriteId = getNewId();
        this.userId = userId;
        this.movieId = movieId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getMovieId() {
        return movieId;
    }

    public void setMovieId(List<Integer> movieId) {
        this.movieId = movieId;
    }

    public void displayData() {
        System.out.printf("ID: %-5d || ID người dung: %-5d", this.favoriteId, this.userId);
        System.out.println("Danh sách phim yêu thích");
        this.movieId.forEach(System.out::println);
    }
    private int getNewId() {
        int max = favoriteList.stream().map(Favorite::getFavoriteId).max(Comparator.naturalOrder()).orElse(0);
        return max + 1;
    }
}
