package com.example.socialmedia;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Dashboard {

    private HelloApplication app;
    private Stage primaryStage;
    private String currentUser;
    private int currentUserId;
    private VBox feedContainer;

    // --- DARK BLUE THEME ---
    private final String CSS_STYLES = """
        .root { -fx-background-color: #0B1437; }
        .header { -fx-background-color: #111C44; -fx-border-color: #2B3674; -fx-border-width: 0 0 1 0; }
        .post-card { -fx-background-color: #111C44; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4); }
        .logo-text { -fx-font-family: 'Segoe UI', serif; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; }
        .username-text { -fx-font-family: 'Segoe UI', sans-serif; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #FFFFFF; }
        .timestamp-text { -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 11px; -fx-text-fill: #A3AED0; }
        .caption-text { -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 14px; -fx-text-fill: #E0E5F2; }
        .stat-card { -fx-background-color: #111C44; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2); }
        .stat-label { -fx-font-size: 13px; -fx-text-fill: #A3AED0; -fx-font-weight: bold; }
        .stat-value { -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; }
        .stat-sub { -fx-font-size: 11px; -fx-text-fill: #01B574; -fx-font-weight: bold; } 
        .icon-btn { -fx-background-color: transparent; -fx-text-fill: #FFFFFF; -fx-font-size: 20px; -fx-cursor: hand; -fx-padding: 5; -fx-min-width: 40; } 
        .icon-btn:hover { -fx-text-fill: #4318FF; -fx-scale-x: 1.1; -fx-scale-y: 1.1; }
        .primary-btn { -fx-background-color: #4318FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 10; -fx-padding: 8 16; }
        .primary-btn:hover { -fx-background-color: #3311CC; }
        .input-field { -fx-background-color: #0B1437; -fx-border-color: #2B3674; -fx-border-radius: 10; -fx-padding: 10; -fx-text-fill: white; }
        .input-field:focused { -fx-border-color: #4318FF; }
        .comment-row { -fx-padding: 8 0; }
        .comment-user { -fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #FFFFFF; }
        .comment-body { -fx-font-size: 13px; -fx-text-fill: #E0E5F2; }
        .delete-link { -fx-text-fill: #FF5B5B; -fx-font-size: 10px; -fx-cursor: hand; -fx-padding: 0 0 0 5; }
        .comment-like-btn { -fx-background-color: transparent; -fx-text-fill: #A3AED0; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 0; }
        .comment-like-btn:hover { -fx-text-fill: #FF5B5B; }
        .search-input { -fx-background-color: #0B1437; -fx-border-color: #2B3674; -fx-border-radius: 20; -fx-padding: 10 20; -fx-text-fill: white; -fx-prompt-text-fill: #A3AED0; -fx-pref-width: 280; }
        .search-input:focused { -fx-border-color: #4318FF; }
        .scroll-pane > .viewport { -fx-background-color: transparent; }
        .scroll-bar:vertical { -fx-pref-width: 0; -fx-opacity: 0; }
        .modal-header { -fx-border-color: #2B3674; -fx-border-width: 0 0 1 0; -fx-padding: 10; }
        .modal-title { -fx-font-family: 'Segoe UI', sans-serif; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white; }
    """;

    public Dashboard(HelloApplication app) {
        this.app = app;
    }

    public void show(Stage stage, String loggedInUser) {
        this.primaryStage = stage;
        this.currentUser = loggedInUser;
        this.currentUserId = getUserId(loggedInUser);

        HBox header = createHeader();

        feedContainer = new VBox(25);
        feedContainer.setPadding(new Insets(30, 0, 30, 0));
        feedContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(feedContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(header, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getStyleClass().add("root");

        loadFeed(null);

        // Set window size
        Scene scene = new Scene(root, 900, 800);
        scene.getStylesheets().add("data:text/css," + CSS_STYLES.replaceAll("\n", ""));
        stage.setScene(scene);
        stage.setTitle("Socialify • " + currentUser);
        stage.show();
    }

    // --- 1. HEADER (Moved Inside with Padding) ---
    private HBox createHeader() {
        Label logo = new Label("Socialify");
        logo.getStyleClass().add("logo-text");
        logo.setMinWidth(Region.USE_PREF_SIZE);

        TextField searchBar = new TextField();
        searchBar.setPromptText("🔍 Search...");
        searchBar.getStyleClass().add("search-input");
        searchBar.setOnAction(e -> {
            String query = searchBar.getText().trim();
            loadFeed(query.isEmpty() ? null : query);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button analyticsBtn = new Button("", getIcon("chart.png", "📊"));
        analyticsBtn.getStyleClass().add("icon-btn");
        analyticsBtn.setTooltip(new Tooltip("Insights"));
        analyticsBtn.setOnAction(e -> showAnalyticsWindow());

        Button addPostBtn = new Button("", getIcon("plus.png", "➕"));
        addPostBtn.getStyleClass().add("icon-btn");
        addPostBtn.setTooltip(new Tooltip("Create Post"));
        addPostBtn.setOnAction(e -> showAddPostWindow());

        Button refreshBtn = new Button("", getIcon("refresh.png", "↻"));
        refreshBtn.getStyleClass().add("icon-btn");
        refreshBtn.setTooltip(new Tooltip("Shuffle Feed"));
        refreshBtn.setOnAction(e -> {
            searchBar.clear();
            loadFeed(null);
            Toast.show("Feed Refreshed!", primaryStage, true);
        });

        Button logoutBtn = new Button("", getIcon("logout.png", "⎋"));
        logoutBtn.getStyleClass().add("icon-btn");
        logoutBtn.setStyle("-fx-text-fill: #FF5B5B;");
        logoutBtn.setOnAction(e -> app.showLoginScreen());

        HBox header = new HBox(20, logo, spacer, searchBar, analyticsBtn, addPostBtn, refreshBtn, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        // --- KEY CHANGE: Increased Padding to move content inside ---
        // Insets(Top, Right, Bottom, Left)
        header.setPadding(new Insets(35, 50, 35, 50));

        header.getStyleClass().add("header");
        return header;
    }

    // --- Helper to load icons ---
    private Node getIcon(String imageName, String fallbackEmoji) {
        try {
            var url = getClass().getResource("/icons/" + imageName);
            if (url != null) {
                ImageView iv = new ImageView(new Image(url.toExternalForm()));
                iv.setFitWidth(24);
                iv.setFitHeight(24);
                return iv;
            }
        } catch (Exception e) {}
        Label fallback = new Label(fallbackEmoji);
        fallback.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        return fallback;
    }

    // --- LOAD FEED ---
    private void loadFeed(String searchQuery) {
        feedContainer.getChildren().clear();
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setStyle(" -fx-progress-color: #4318FF;");
        VBox loadingBox = new VBox(spinner);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(50));
        feedContainer.getChildren().add(loadingBox);

        Task<List<PostData>> task = new Task<>() {
            @Override
            protected List<PostData> call() throws Exception {
                List<PostData> data = new ArrayList<>();
                String query;
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    query = "SELECT p.post_id, p.caption, p.location, p.image_path, p.created_at, u.username, u.profile_photo_url " +
                            "FROM post p JOIN users u ON p.user_id = u.user_id " +
                            "WHERE p.caption LIKE ? OR p.location LIKE ? OR u.username LIKE ? " +
                            "ORDER BY p.created_at DESC LIMIT 50";
                } else {
                    query = "SELECT p.post_id, p.caption, p.location, p.image_path, p.created_at, u.username, u.profile_photo_url " +
                            "FROM post p JOIN users u ON p.user_id = u.user_id ORDER BY RAND() LIMIT 15";
                }

                try (Connection c = DatabaseConnection.getConnection();
                     PreparedStatement p = c.prepareStatement(query)) {
                    if (searchQuery != null && !searchQuery.isEmpty()) {
                        String s = "%" + searchQuery + "%";
                        p.setString(1, s); p.setString(2, s); p.setString(3, s);
                    }
                    ResultSet rs = p.executeQuery();
                    while (rs.next()) {
                        data.add(new PostData(
                                rs.getInt("post_id"), rs.getString("username"), rs.getString("caption"),
                                rs.getString("location"), rs.getString("profile_photo_url"),
                                rs.getString("image_path"), rs.getTimestamp("created_at")
                        ));
                    }
                }
                return data;
            }
        };

        task.setOnSucceeded(e -> {
            feedContainer.getChildren().clear();
            List<PostData> posts = task.getValue();
            if (posts.isEmpty()) {
                Label empty = new Label(searchQuery != null ? "No results found." : "No posts available!");
                empty.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                feedContainer.getChildren().add(empty);
            } else {
                for (PostData p : posts) {
                    feedContainer.getChildren().add(createPostCard(p.postId, p.username, p.caption, p.location, p.photoUrl, p.imagePath, p.createdAt));
                }
                Label endLbl = new Label(searchQuery != null ? "End of results." : "Showing 15 random posts. Click ↻ to shuffle.");
                endLbl.setStyle("-fx-text-fill: #A3AED0; -fx-padding: 20; -fx-font-size: 12px;");
                feedContainer.getChildren().add(endLbl);
            }
        });

        task.setOnFailed(e -> {
            feedContainer.getChildren().clear();
            Toast.show("Failed to load feed!", primaryStage, false);
        });
        new Thread(task).start();
    }

    private static class PostData {
        int postId;
        String username, caption, location, photoUrl, imagePath;
        Timestamp createdAt;
        public PostData(int id, String u, String c, String l, String p, String i, Timestamp t) {
            this.postId=id; this.username=u; this.caption=c; this.location=l; this.photoUrl=p; this.imagePath=i; this.createdAt=t;
        }
    }

    private VBox createPostCard(int postId, String user, String caption, String loc, String photoUrl, String postImgPath, Timestamp created) {
        VBox card = new VBox(0);
        card.getStyleClass().add("post-card");
        card.setMaxWidth(470);

        ScaleTransition lift = new ScaleTransition(Duration.millis(200), card);
        lift.setFromX(1.0); lift.setFromY(1.0);
        lift.setToX(1.02); lift.setToY(1.02);

        card.setOnMouseEntered(e -> {
            lift.playFromStart();
            card.setStyle("-fx-background-color: #1B2559; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 8);");
        });
        card.setOnMouseExited(e -> {
            lift.stop();
            card.setScaleX(1.0); card.setScaleY(1.0);
            card.setStyle("-fx-background-color: #111C44; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 4);");
        });

        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(12));

        String safePhotoUrl = (photoUrl != null && !photoUrl.isEmpty()) ? photoUrl : "https://cdn-icons-png.flaticon.com/512/149/149071.png";
        ImageView profileView = new ImageView(new Image(safePhotoUrl, 32, 32, true, true, true));
        profileView.setClip(new Circle(16, 16, 16));

        VBox meta = new VBox(2);
        Label userLabel = new Label(user); userLabel.getStyleClass().add("username-text");
        Label locLabel = new Label(loc != null ? loc : ""); locLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #A3AED0;");
        meta.getChildren().addAll(userLabel);
        if(!locLabel.getText().isEmpty()) meta.getChildren().add(locLabel);

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(profileView, meta, spacer);

        if (user.equals(currentUser)) {
            MenuButton options = new MenuButton("•••");
            options.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: white;");
            MenuItem del = new MenuItem("Delete"); del.setOnAction(e -> deletePost(postId));
            MenuItem edit = new MenuItem("Edit"); edit.setOnAction(e -> updatePost(postId, caption));
            options.getItems().addAll(edit, del);
            headerBox.getChildren().add(options);
        }

        VBox imgBox = new VBox();
        imgBox.setAlignment(Pos.CENTER);
        imgBox.setStyle("-fx-border-color: #2B3674; -fx-border-width: 1 0 1 0;");
        if (postImgPath != null && !postImgPath.isEmpty()) {
            try {
                Image img = resolveImage(postImgPath);
                if(img != null) {
                    ImageView view = new ImageView(img);
                    view.setFitWidth(468); view.setPreserveRatio(true);
                    imgBox.getChildren().add(view);
                }
            } catch(Exception e){}
        }

        boolean initialLiked = isLiked(postId);
        int initialCount = getLikeCount(postId);
        AtomicBoolean likedState = new AtomicBoolean(initialLiked);
        AtomicInteger countState = new AtomicInteger(initialCount);

        Button likeBtn = new Button(likedState.get() ? "❤️" : "♡");
        likeBtn.getStyleClass().add("icon-btn");
        if(likedState.get()) likeBtn.setStyle("-fx-text-fill: #FF5B5B;");

        Label likesLabel = new Label(countState.get() > 0 ? countState.get() + " likes" : "");
        likesLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        likeBtn.setOnAction(e -> {
            boolean newState = !likedState.get();
            likedState.set(newState);
            likeBtn.setText(newState ? "❤️" : "♡");
            likeBtn.setStyle(newState ? "-fx-text-fill: #FF5B5B;" : "-fx-text-fill: white;");
            int newCount = newState ? countState.incrementAndGet() : countState.decrementAndGet();
            likesLabel.setText(newCount > 0 ? newCount + " likes" : "");
            new Thread(() -> {
                try(Connection c=DatabaseConnection.getConnection()){
                    if(newState) c.createStatement().executeUpdate("INSERT INTO post_likes VALUES ("+postId+","+currentUserId+")");
                    else c.createStatement().executeUpdate("DELETE FROM post_likes WHERE post_id="+postId+" AND user_id="+currentUserId);
                } catch(Exception ex){ ex.printStackTrace(); }
            }).start();
        });

        Button commentBtn = new Button("💬");
        commentBtn.getStyleClass().add("icon-btn");
        commentBtn.setOnAction(e -> showCommentsWindow(postId));

        HBox actions = new HBox(15, likeBtn, commentBtn);
        actions.setPadding(new Insets(10, 12, 0, 12));

        VBox footer = new VBox(5);
        footer.setPadding(new Insets(8, 12, 16, 12));
        footer.getChildren().add(likesLabel);

        if(caption != null && !caption.isEmpty()) {
            Label cap = new Label(user + " " + caption);
            cap.getStyleClass().add("caption-text");
            cap.setWrapText(true);
            footer.getChildren().add(cap);
        }
        if (created != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • HH:mm");
            Label timeLabel = new Label(sdf.format(created));
            timeLabel.getStyleClass().add("timestamp-text");
            footer.getChildren().add(timeLabel);
        }
        card.getChildren().addAll(headerBox, imgBox, actions, footer);
        return card;
    }

    // --- MODALS ---
    private void showAnalyticsWindow() {
        Stage window = new Stage();
        window.initOwner(primaryStage);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Analytics");
        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #0B1437;");
        Label title = new Label("Overview");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        String mostLiked = getMostLikedPost();
        double avgPosts = getAvgPostsPerUser();
        int inactive = getInactiveUserCount();
        grid.add(createAnalyticsCard("Most Popular", mostLiked, "Top performing"), 0, 0);
        grid.add(createAnalyticsCard("Avg Posts/User", String.format("%.1f", avgPosts), "Engagement"), 1, 0);
        grid.add(createAnalyticsCard("Inactive Users", String.valueOf(inactive), "Alert"), 0, 1);
        VBox layout = new VBox(20, title, grid);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #0B1437;");
        Scene scene = new Scene(layout, 500, 400);
        scene.getStylesheets().add("data:text/css," + CSS_STYLES.replaceAll("\n", ""));
        window.setScene(scene);
        window.show();
    }

    private VBox createAnalyticsCard(String label, String value, String subtext) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(200);
        Label lbl = new Label(label.toUpperCase()); lbl.getStyleClass().add("stat-label");
        Label val = new Label(value); val.getStyleClass().add("stat-value"); val.setWrapText(true);
        Label sub = new Label("• " + subtext); sub.getStyleClass().add("stat-sub");
        card.getChildren().addAll(lbl, val, sub);
        return card;
    }

    private void showCommentsWindow(int postId) {
        Stage window = new Stage();
        window.initOwner(primaryStage);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Comments");
        VBox commentContainer = new VBox(15);
        commentContainer.setPadding(new Insets(15));
        commentContainer.setStyle("-fx-background-color: #111C44;");
        ScrollPane scroll = new ScrollPane(commentContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        refreshComments(commentContainer, postId);
        TextField input = new TextField();
        input.setPromptText("Add a comment...");
        input.getStyleClass().add("input-field");
        HBox.setHgrow(input, Priority.ALWAYS);
        Button postBtn = new Button("Post");
        postBtn.setStyle("-fx-text-fill: #4318FF; -fx-font-weight: bold; -fx-background-color: transparent; -fx-cursor: hand;");
        postBtn.setOnAction(e -> {
            String txt = input.getText().trim();
            if (txt.isEmpty()) {
                Toast.show("Comment cannot be empty!", window, false);
            } else {
                addComment(postId, txt);
                input.clear();
                refreshComments(commentContainer, postId);
            }
        });
        HBox inputBar = new HBox(10, input, postBtn);
        inputBar.setPadding(new Insets(15));
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setStyle("-fx-border-color: #2B3674; -fx-border-width: 1 0 0 0; -fx-background-color: #111C44;");
        VBox layout = new VBox(scroll, inputBar);
        Scene scene = new Scene(layout, 400, 500);
        scene.getStylesheets().add("data:text/css," + CSS_STYLES.replaceAll("\n", ""));
        window.setScene(scene);
        window.show();
    }

    private void refreshComments(VBox container, int postId) {
        container.getChildren().clear();
        String sql = "SELECT c.comment_id, c.comment_text, c.user_id, u.username, u.profile_photo_url FROM comments c JOIN users u ON c.user_id = u.user_id WHERE c.post_id = ? ORDER BY c.created_at ASC";
        try(Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)){
            p.setInt(1, postId);
            ResultSet rs = p.executeQuery();
            while(rs.next()) {
                container.getChildren().add(createCommentRow(
                        rs.getInt("comment_id"), rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("comment_text"), rs.getString("profile_photo_url"), postId, container
                ));
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    private HBox createCommentRow(int commentId, int userId, String user, String text, String photoUrl, int postId, VBox container) {
        HBox row = new HBox(10);
        row.getStyleClass().add("comment-row");
        row.setAlignment(Pos.TOP_LEFT);
        String safeUrl = (photoUrl != null && !photoUrl.isEmpty()) ? photoUrl : "https://cdn-icons-png.flaticon.com/512/149/149071.png";
        ImageView av = new ImageView(new Image(safeUrl, 32, 32, true, true, true));
        av.setClip(new Circle(16, 16, 16));
        VBox content = new VBox(2);
        HBox textLine = new HBox(5);
        Label uLbl = new Label(user); uLbl.getStyleClass().add("comment-user");
        Label tLbl = new Label(text); tLbl.getStyleClass().add("comment-body");
        tLbl.setWrapText(true); tLbl.setMaxWidth(260);
        textLine.getChildren().addAll(uLbl, tLbl);
        content.getChildren().add(textLine);
        if (userId == currentUserId) {
            Label delLbl = new Label("Delete");
            delLbl.getStyleClass().add("delete-link");
            delLbl.setOnMouseClicked(e -> {
                deleteComment(commentId);
                refreshComments(container, postId);
            });
            content.getChildren().add(delLbl);
        }
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        boolean liked = isCommentLiked(commentId);
        int likes = getCommentLikeCount(commentId);
        AtomicBoolean cLiked = new AtomicBoolean(liked);
        AtomicInteger cCount = new AtomicInteger(likes);
        VBox likeBox = new VBox(0); likeBox.setAlignment(Pos.CENTER);
        Button cLikeBtn = new Button(cLiked.get() ? "❤️" : "♡");
        cLikeBtn.getStyleClass().add("comment-like-btn");
        if(cLiked.get()) cLikeBtn.setStyle("-fx-text-fill: #FF5B5B;");
        Label cLikeCount = new Label(cCount.get() > 0 ? String.valueOf(cCount.get()) : "");
        cLikeCount.setStyle("-fx-font-size: 10px; -fx-text-fill: #A3AED0;");
        cLikeBtn.setOnAction(e -> {
            boolean newState = !cLiked.get();
            cLiked.set(newState);
            cLikeBtn.setText(newState ? "❤️" : "♡");
            cLikeBtn.setStyle(newState ? "-fx-text-fill: #FF5B5B;" : "-fx-text-fill: #A3AED0;");
            int newC = newState ? cCount.incrementAndGet() : cCount.decrementAndGet();
            cLikeCount.setText(newC > 0 ? String.valueOf(newC) : "");
            new Thread(() -> toggleCommentLike(commentId)).start();
        });
        likeBox.getChildren().addAll(cLikeBtn, cLikeCount);
        row.getChildren().addAll(av, content, spacer, likeBox);
        return row;
    }

    private void deleteComment(int commentId) {
        try(Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM comments WHERE comment_id = ?")) {
            p.setInt(1, commentId); p.executeUpdate();
        } catch(Exception e) { e.printStackTrace(); }
    }

    private void showAddPostWindow() {
        Stage window = new Stage();
        window.initOwner(primaryStage);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create Post");
        Label title = new Label("Create new post");
        title.getStyleClass().add("modal-title");
        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("modal-header");
        TextArea caption = new TextArea();
        caption.setPromptText("Write a caption..."); caption.setWrapText(true); caption.setPrefRowCount(4);
        caption.setStyle("-fx-control-inner-background: #0B1437; -fx-text-fill: white; -fx-border-color: #2B3674; -fx-background-color: transparent;");
        TextField loc = new TextField(); loc.setPromptText("Add Location"); loc.getStyleClass().add("input-field");
        TextField img = new TextField(); img.setPromptText("Image Path / URL"); img.getStyleClass().add("input-field");
        Button shareBtn = new Button("Share");
        shareBtn.getStyleClass().add("primary-btn");
        shareBtn.setMaxWidth(Double.MAX_VALUE);
        shareBtn.setOnAction(e -> {
            if(caption.getText().trim().isEmpty() && img.getText().trim().isEmpty()) {
                Toast.show("Caption or Image is required!", window, false);
                return;
            }
            savePost(caption.getText(), loc.getText(), img.getText());
            loadFeed(null);
            window.close();
            Toast.show("Post Shared!", primaryStage, true);
        });
        VBox form = new VBox(15, caption, loc, img, new Separator(), shareBtn);
        form.setPadding(new Insets(20));
        VBox layout = new VBox(header, form);
        layout.setStyle("-fx-background-color: #111C44;");
        Scene scene = new Scene(layout, 400, 450);
        scene.getStylesheets().add("data:text/css," + CSS_STYLES.replaceAll("\n", ""));
        window.setScene(scene);
        window.show();
    }

    // --- HELPERS ---
    private Image resolveImage(String path) {
        if (path.startsWith("http")) return new Image(path, true);
        else if (path.startsWith("file:") || path.startsWith("/") || path.matches("^[a-zA-Z]:.*")) {
            return new Image(path.startsWith("file:") ? path : "file:" + path, true);
        } else {
            var res = getClass().getResource("/" + path);
            return (res != null) ? new Image(res.toExternalForm(), true) : null;
        }
    }
    private int getUserId(String username) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT user_id FROM users WHERE username=?")){p.setString(1,username); ResultSet rs=p.executeQuery(); if(rs.next()) return rs.getInt(1);}catch(Exception e){} return -1;}
    private int getLikeCount(int pid) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM post_likes WHERE post_id=?")){p.setInt(1,pid); ResultSet rs=p.executeQuery(); if(rs.next()) return rs.getInt(1);}catch(Exception e){} return 0;}
    private boolean isLiked(int pid) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM post_likes WHERE post_id=? AND user_id=?")){p.setInt(1,pid); p.setInt(2,currentUserId); return p.executeQuery().next();}catch(Exception e){} return false;}
    private void addComment(int pid, String t) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("INSERT INTO comments (post_id,user_id,comment_text) VALUES (?,?,?)")){p.setInt(1,pid);p.setInt(2,currentUserId);p.setString(3,t);p.executeUpdate();}catch(Exception e){}}
    private void savePost(String c, String l, String i) { try(Connection con=DatabaseConnection.getConnection(); PreparedStatement p=con.prepareStatement("INSERT INTO post (user_id,caption,location,image_path) VALUES (?,?,?,?)")){p.setInt(1,currentUserId);p.setString(2,c);p.setString(3,l);p.setString(4,i);p.executeUpdate();}catch(Exception e){}}
    private void deletePost(int pid) { try(Connection c=DatabaseConnection.getConnection()){ c.createStatement().executeUpdate("DELETE FROM post_likes WHERE post_id="+pid); c.createStatement().executeUpdate("DELETE FROM comments WHERE post_id="+pid); c.createStatement().executeUpdate("DELETE FROM post WHERE post_id="+pid); loadFeed(null);}catch(Exception e){}}
    private void updatePost(int pid, String old) { new EditPostDialog(pid, old, () -> loadFeed(null)).show(); }

    private String getMostLikedPost() {
        String result = "No data";
        String q = "SELECT p.caption, COUNT(l.post_id) as likes FROM post p JOIN post_likes l ON p.post_id = l.post_id GROUP BY p.post_id ORDER BY likes DESC LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection(); ResultSet rs = c.createStatement().executeQuery(q)) {
            if(rs.next()) { String cap = rs.getString("caption"); if(cap.length()>20) cap=cap.substring(0,20)+"..."; result = cap + "\n(" + rs.getInt("likes") + " likes)"; }
        } catch(Exception e) {} return result;
    }
    private double getAvgPostsPerUser() { try (Connection c = DatabaseConnection.getConnection(); ResultSet rs = c.createStatement().executeQuery("SELECT (SELECT COUNT(*) FROM post) / (SELECT COUNT(*) FROM users)")) { if(rs.next()) return rs.getDouble(1); } catch(Exception e) {} return 0.0; }
    private int getInactiveUserCount() { try (Connection c = DatabaseConnection.getConnection(); ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM users WHERE user_id NOT IN (SELECT DISTINCT user_id FROM post)")) { if(rs.next()) return rs.getInt(1); } catch(Exception e) {} return 0; }

    private boolean isCommentLiked(int cid) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM comment_likes WHERE comment_id=? AND user_id=?")){p.setInt(1,cid); p.setInt(2,currentUserId); return p.executeQuery().next();}catch(Exception e){} return false;}
    private int getCommentLikeCount(int cid) { try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM comment_likes WHERE comment_id=?")){p.setInt(1,cid); ResultSet rs=p.executeQuery(); if(rs.next()) return rs.getInt(1);}catch(Exception e){} return 0;}
    private void toggleCommentLike(int cid) { try(Connection c=DatabaseConnection.getConnection()){ boolean l=isCommentLiked(cid); c.createStatement().executeUpdate(l?"DELETE FROM comment_likes WHERE comment_id="+cid+" AND user_id="+currentUserId : "INSERT INTO comment_likes VALUES ("+cid+","+currentUserId+")");}catch(Exception e){}}
}