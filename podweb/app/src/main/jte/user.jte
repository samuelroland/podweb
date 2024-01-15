@import podweb.models.Episode
@import podweb.models.EpisodeSearch
@import podweb.models.User
@import java.util.ArrayList

@param User user
@param Object loggedUser

@template.layout(
content = @`


    <div class="user">
        <h4 class="text-blue-500">${"Page of : " + user.firstname}</h4>
    </div>

`,
loggedUser
)