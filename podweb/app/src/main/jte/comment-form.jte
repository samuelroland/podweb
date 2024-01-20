@param Boolean respond = false
@param int shift

!{var css = respond ? "mt-2 ml-4" : "mt-10";}

<form class="${css} bg-slate-200 rounded-md p-2" style="margin-left: ${shift}px" method="post">
    <div class="flex">
        <h4 type="text" class="flex-1">${respond ? "Respond to comment" : "Leave a comment"}</h4>
        <button type="submit">Send</button>
    </div>

    <textarea type="text" class="w-full p-1" placeholder="Write a comment"></textarea>
    <br>
</form>
