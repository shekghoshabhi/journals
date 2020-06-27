package model;

import android.app.Application;

import com.google.firebase.Timestamp;

public class journalModel {

        private  String title ;
        private String description;
        private String  ImageUrl ;
        private String userName;
        private String userId;
        private Timestamp timeAdded ;

        public journalModel(){}

        public journalModel(String title, String description, String imageUrl, String userName, String userId, Timestamp timeAdded) {
            this.title = title;
            this.description = description;
            this.ImageUrl = imageUrl;
            this.userName = userName;
            this.userId = userId;
            this.timeAdded = timeAdded;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Timestamp getTimeAdded() {
            return timeAdded;
        }

        public void setTimeAdded(Timestamp timeAdded) {
            this.timeAdded = timeAdded;
        }
    }




