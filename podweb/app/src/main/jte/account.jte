@import podweb.models.User

@param User user

<div>
    @if(user != null)
    <div class="text-xl">${user.firstname} ${user.lastname}</div>
    @else
    <div class="bg-red-200"></div>
    <button>Login</button>
    @endif
</div>