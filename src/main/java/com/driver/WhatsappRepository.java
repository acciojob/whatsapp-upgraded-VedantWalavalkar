package com.driver;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WhatsappRepository {
    Map<String,User> userMap = new HashMap<>();

    Map<String,Group> groupMap = new HashMap<>();
    int groupCount = 1;

    Map<Integer,Message> messageMap = new HashMap<>();
    int messageId = 1;

    public String createUser(String name, String mobile) throws Exception {
        if(userMap.containsKey(mobile))
            throw new Exception("User already exists");
        // create User
        User user = new User();
        user.setMobile(mobile);
        user.setName(name);

        userMap.put(mobile,user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group = new Group();
        if(users.size() == 2){
            User user2 = users.get(1);
            group.setName(user2.getName());
            group.setNumberOfParticipants(2);
            group.setUsers(users);
            groupMap.put(group.getName(),group);
            return group;
        }
        String groupName = "Group " + groupCount;
        groupCount++;
        group.setName(groupName);
        group.setNumberOfParticipants(users.size());
        group.setUsers(users);
//        for(User user : users){
//            user.setGroup(group);
//        }
        return group;
    }

    public int createMessage(String content) {
        Message message = new Message();
        message.setContent(content);
        message.setId(messageId);
        messageId++;
        Date todayDate = new Date();
        message.setTimestamp(todayDate);

        messageMap.put(messageId,message);

        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        String groupName = group.getName();
        if(!groupMap.containsKey(groupName))
            throw new Exception("Group does not exist");

        List<User> users = group.getUsers();
        boolean userPresent = false;
        for(User user : users){
            if(user == sender) userPresent = true;
        }
        if(!userPresent)
            throw new Exception("You are not allowed to send message");

        List<Message> messages = group.getMessages();
        messages.add(message);
        sender.getMessageList().add(message);
        message.setUser(sender);

        groupMap.put(groupName, group);

        return messages.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        String groupName = group.getName();
        if(!groupMap.containsKey(groupName))
            throw new Exception("Group does not exist");

        List<User> users = group.getUsers();
        if(approver != approver)
            throw new Exception("Approver does not have rights");

        int userIdx = -1;
        for(int i = 0 ; i<users.size() ; i++){
            if(user == users.get(i)) {
                userIdx = i;
                break;
            }
        }
        if(userIdx == -1)
            throw new Exception("User is not a participant");

        // swap user and admin
        User temp = users.get(0);
        users.set(0,users.get(userIdx));
        users.set(userIdx,temp);

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        Group group = null;
        for(String groupName : groupMap.keySet())
        {
            group = groupMap.get(groupName);
            List<User> users = group.getUsers();
            for(User u : users){
                if(user == u)
                    break;
            }
        }
        if(group == null)
            throw new Exception("User not found");

        List<User> users = group.getUsers();
        if(users.get(0) == user)
            throw new Exception("Cannot remove admin");

        int userIdx = -1;
        for(int i = 0 ; i<users.size() ; i++){
            if(user == users.get(i))
                userIdx = i;
        }
        users.remove(userIdx);
        group.setNumberOfParticipants(group.getNumberOfParticipants() - 1);

        int updateNoOfMessages = 0;
        List<Message> messages = group.getMessages();
        for(Message m : messages){
            if(m.getUser() == user) {
                messages.remove(m);
                updateNoOfMessages++;
            }
        }

        int noOfUsers = group.getNumberOfParticipants();
        int overallMessages = group.getMessages().size();

        return updateNoOfMessages + noOfUsers + overallMessages;
    }
}
