package comp5216.sydney.edu.au.project_flume.Fragments;

import comp5216.sydney.edu.au.project_flume.Notification.MyRespond;
import comp5216.sydney.edu.au.project_flume.Notification.NotificationSender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfCQd2bk:APA91bFSUXdZbWn2Sxc3ny4xOhwoGtjvI4uA179fy-jnTbGeLTjuh8ZdusuQ3DZmGb5i4xFzIwbbtF0J6Vhwdh3wY_-DZDi1vNRKZhMYRjKNXsP4TsWZHmwu8aLoH6IBuoYFLYBGOVkR"
            }
    )

    @POST("fcm/send")
    Call<MyRespond> sendNotification(@Body NotificationSender body);

}
