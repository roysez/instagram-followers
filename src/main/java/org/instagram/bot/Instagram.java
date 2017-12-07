package org.instagram.bot;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Instagram {

    private int followed;
    private Instagram4j instagram;
    private InstagramSearchUsernameResult target;
    private List<String> listOfFollowedUsers = new ArrayList<>();

    public Instagram(String login, String password) {
        instagram = Instagram4j.builder().username(login).password(password).build();
        setup();
    }

    private void setup() {
        instagram.setup();
        try {
            instagram.login();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTarget(String username) throws IOException {
        target = instagram.sendRequest(new InstagramSearchUsernameRequest(username));

        System.out.println("ID for @" + username + " is " + target.getUser().getPk());
        System.out.println("Number of followers: " + target.getUser().getFollower_count());

    }

    private void listOfFollowers() throws IOException {

        listOfFollowedUsers.addAll(instagram.sendRequest(
                new InstagramGetUserFollowingRequest(
                        instagram.sendRequest(new InstagramSearchUsernameRequest(instagram.getUsername()))
                                .getUser().getPk()))
                .getUsers()
                .stream()
                .map(InstagramUserSummary::getUsername)
                .collect(Collectors.toList()));

        System.out.println("Followed count before start:" + listOfFollowedUsers.size());
    }

    public void start() throws IOException {
        Optional<InstagramSearchUsernameResult> target = Optional.ofNullable(this.target);


        if (target.isPresent()) {

            listOfFollowers();
            Random random = new Random();
            InstagramGetUserFollowersResult followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(target.get().getUser().getPk()));
            List<InstagramUserSummary> users = followers.getUsers();
            for (InstagramUserSummary user : users) {
                if (!listOfFollowedUsers.contains(user.getUsername())) {
                    System.out.println("User " + user.getUsername() + " followed!\n " + " FOLLOWED: " + followed++);

                    instagram.sendRequest(new InstagramFollowRequest(user.getPk()));

                    try {
                        TimeUnit.SECONDS.sleep(random.nextInt(60 - 41) + 41);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
