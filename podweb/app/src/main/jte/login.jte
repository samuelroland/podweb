@import podweb.models.Episode
@import podweb.models.Podcast
@import podweb.models.User
@import java.util.ArrayList

@param boolean error

@template.layout(
content = @`
<div class="w-64 m-auto">

    <h1>Login</h1>
    <form action="/login" method="post">
        <label for="">Email</label>
        <input class="block my-3" type="email" name="email">
        <label for="">Password</label>
        <input class="block my-3" type="password" name="password">
        <button type="submit">Submit</button>
    </form>

    @if(error)
    <p class="text-red-600">Login error</p>
    @endif
</div>
`,
null
)