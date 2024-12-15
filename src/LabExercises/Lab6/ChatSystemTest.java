//package LabExercises.Lab6;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class NoSuchRoomException extends Exception{
    NoSuchRoomException(String roomName){
        super(String.format("NoSuchRoomException %s",roomName));
    }
}
class NoSuchUserException extends Exception{
    NoSuchUserException(String username){
        super(String.format("NoSuchUserException %s",username));
    }
}

class ChatRoom{
    private String name;
    private Set<String> users;

    ChatRoom(String name) {
        this.name=name;
        this.users=new TreeSet<>();
    }

    public void addUser(String user) {
        users.add(user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    @Override
    public String toString() {
        if (users.isEmpty()) {
            return name + "\nEMPTY\n";
        }
        return name + "\n" + String.join("\n", users)+"\n";
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }
    public int numUsers(){
        return users.size();
    }
    public String getName(){
        return name;
    }
}

class ChatSystem{
    private Map<String,ChatRoom> rooms;
    private Set<String> users;
    ChatSystem(){
        this.rooms=new TreeMap<>();
        this.users=new TreeSet<>();
    }
    public void addRoom(String roomName){
        rooms.putIfAbsent(roomName,new ChatRoom(roomName));
    }
    public void removeRoom(String roomName){
        rooms.remove(roomName);
    }
    public ChatRoom getRoom(String roomName){
        return rooms.get(roomName);
    }
    public void register(String userName){
        if (users.contains(userName)){
            return;
        }
        users.add(userName);
        ChatRoom result= rooms.values().stream()
                .min(Comparator.comparingInt(ChatRoom::numUsers).thenComparing(ChatRoom::getName))
                .orElse(null);
        if (result!=null){
            result.addUser(userName);

        }
    }
    public void registerAndJoin(String username, String roomName) {
        register(username);
        ChatRoom room = getRoom(roomName);
        if (room != null) {
            room.addUser(username);
        }
    }
    public void joinRoom(String username,String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(username)){
            throw new NoSuchUserException(username);
        }
        if (rooms.get(roomName) !=null){
            rooms.get(roomName).addUser(username);
        }else{
            throw new NoSuchRoomException(roomName);
        }
    }

    public void leaveRoom(String username,String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(username)){
            throw  new NoSuchUserException(username);
        }
        if (rooms.get(roomName)!=null){
            rooms.get(roomName).removeUser(username);
        }else{
            throw new NoSuchRoomException(roomName);
        }
    }
    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if (!users.contains(username)) {
            throw new NoSuchUserException(username);
        }
        if (!users.contains(friend_username)) {
            throw new NoSuchUserException(friend_username);
        }
        rooms.values().forEach(chatRoom -> {
            if (chatRoom.hasUser(friend_username)) {
                chatRoom.addUser(username);
            }
        });
    }


}
public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}
