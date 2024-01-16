@import podweb.models.User

@param User user
@param Object loggedUser

@template.layout(
content = @`

    <div class="flex flex-wrap">
    <h4 class="text-blue-500">${"Page of : " + user.firstname}</h4>
    </div>

`,
loggedUser
)