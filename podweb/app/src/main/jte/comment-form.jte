@param Boolean respond = false
@param int episodeId
@param int shift
@param boolean logged
@param Integer parent_id = null

!{var css = respond ? "mt-2 ml-4" : "mt-10";}


<form class="${css} bg-slate-200 rounded-md p-2" style="margin-left: ${shift}px" method="post" action="/episodes/${episodeId}/comments">
    <div class="flex">
        <h4 type="text" class="flex-1">${respond ? "Respond to comment" : "Leave a comment"}</h4>
        <input disabled="${!logged}" type="number" min="0" max="5" name="note" class="w-16 mx-4 h-5">
        <input type="hidden" name="episode_id" value="${episodeId}">
        <input type="hidden" name="parent_id" value="${parent_id}">
        <button type="submit" disabled="${!logged}">Send</button>
    </div>

    <textarea name="content" disabled="${!logged}" type="text" class="w-full p-1" placeholder="Write a comment"></textarea>
    <br>
</form>

@if(logged == false)
<span class="text-red-500">Please login before commenting...</span>
@endif