@import podweb.models.Episode
@import podweb.models.EpisodeSearch
@import podweb.models.User
@import java.util.ArrayList

@param ArrayList<User> users
@param Object loggedUser

@template.layout(
content = @`
    <h1>Users of Podweb</h1>
    <ul>
        @for(User user : users)
        <li class="list-disc ml-3">
            $unsafe{user.link()}
        </li>
        @endfor
    </ul>
`,
loggedUser
)