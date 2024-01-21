@import podweb.models.Comment
@import podweb.models.Episode
@import podweb.models.User
@import podweb.models.Listen
@import java.util.ArrayList
@import java.util.Map

@param Map<Integer, User> authors
@param ArrayList<Comment> comments
@param Episode episode
@param Object loggedUser
@param Listen listen
@param Map<Integer, Integer> commentsLevelById

!{var logged = loggedUser instanceof User;}

@template.layout(
content = @`
@template.episode-details(episode=episode, withDetails=true, logged=logged, listen=listen)

<h2 class="mt-10">Comments (${comments.size()})</h2>
@if (comments.isEmpty())
<div class="flex flex-wrap">
    <h4 class="text-blue-500">$unsafe{"No comments here ! :-("}</h4>
</div>
<br>
@endif

!{var loggedUserId = logged ? ((User) loggedUser).id : -1;}
!{var PIXEL_SHIFT = 40;}
@for(Comment c: comments)
    !{var shift = PIXEL_SHIFT * (commentsLevelById.get(c.id) -1); }
    <div class="my-5" style="margin-left: ${shift}px" x-data="{form: false}">
        <div class="flex items-center">
            <div class="flex-1 text-gray-600">By $unsafe{authors.get(c.user_id).link()} on ${c.date()}</div>
            <div class="text-gray-800 flex items-center">
                @for (int i = 0; i < c.note; i++)
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                    fill="currentColor" class="w-6 h-6 text-blue-600">
                    <path fill-rule="evenodd"
                        d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z"
                        clip-rule="evenodd" />
                </svg>
                @endfor

                <!-- reply icon -->
                <span @click="form = !form" class="ml-4 hover:bg-blue-200 p-1">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-6 h-6">
                        <path fill-rule="evenodd" d="M9.53 2.47a.75.75 0 0 1 0 1.06L4.81 8.25H15a6.75 6.75 0 0 1 0 13.5h-3a.75.75 0 0 1 0-1.5h3a5.25 5.25 0 1 0 0-10.5H4.81l4.72 4.72a.75.75 0 1 1-1.06 1.06l-6-6a.75.75 0 0 1 0-1.06l6-6a.75.75 0 0 1 1.06 0Z" clip-rule="evenodd" />
                      </svg>
                </span>

                @if(logged && loggedUserId == c.user_id)
                <form action="/comments/${c.id}/delete" method="post">
                    <button type="submit" href="/comments/${c.id}/delete" class="ml-2 hover:bg-blue-200 p-1">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-6 h-6">
                            <path fill-rule="evenodd" d="M16.5 4.478v.227a48.816 48.816 0 0 1 3.878.512.75.75 0 1 1-.256 1.478l-.209-.035-1.005 13.07a3 3 0 0 1-2.991 2.77H8.084a3 3 0 0 1-2.991-2.77L4.087 6.66l-.209.035a.75.75 0 0 1-.256-1.478A48.567 48.567 0 0 1 7.5 4.705v-.227c0-1.564 1.213-2.9 2.816-2.951a52.662 52.662 0 0 1 3.369 0c1.603.051 2.815 1.387 2.815 2.951Zm-6.136-1.452a51.196 51.196 0 0 1 3.273 0C14.39 3.05 15 3.684 15 4.478v.113a49.488 49.488 0 0 0-6 0v-.113c0-.794.609-1.428 1.364-1.452Zm-.355 5.945a.75.75 0 1 0-1.5.058l.347 9a.75.75 0 1 0 1.499-.058l-.346-9Zm5.48.058a.75.75 0 1 0-1.498-.058l-.347 9a.75.75 0 0 0 1.5.058l.345-9Z" clip-rule="evenodd" />
                            </svg>
                    </button>
                </form>

                @endif
            </div>
        </div>
        <div class="my-2">${c.content}</div>
    
        <div x-show="form">
            @template.comment-form(respond=true, shift=PIXEL_SHIFT, episodeId=episode.id, logged=logged, parent_id=c.id)
        </div>
    </div>
@endfor

@template.comment-form(respond=false, shift=0, episodeId=episode.id, logged=logged)
`,
loggedUser
)