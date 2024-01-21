@import podweb.models.User

@param Object user

<div>
    @if(user instanceof User u)
    <span class="text-lg mr-5 font-bold">$unsafe{u.link()}</span>
    <a href="/logout"><button>Logout</button></a>
    @else
    <div class="bg-red-200"></div>
    <a href="/login"><button>Login</button></a>
    @endif
</div>