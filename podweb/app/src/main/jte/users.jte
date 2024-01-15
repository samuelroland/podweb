@import podweb.models.Episode
@import podweb.models.EpisodeSearch
@import podweb.models.User
@import java.util.ArrayList

@param ArrayList<User> users
@param Object loggedUser

@template.layout(
content = @`

    @for(User u : users)
        <div class="user">
            <div class="user-name">
        <a href="/user/${u.id}" class="text-blue-500">
            <h4 class="text-blue-500">${"User : " + u.firstname}</h4>
        </a>
            </div>
        </div>
    @endfor
`,
loggedUser
)