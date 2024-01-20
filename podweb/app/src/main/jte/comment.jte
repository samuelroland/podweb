@import podweb.models.Comment
@import podweb.models.Episode
@import podweb.models.User
@import java.util.ArrayList

@param Episode episode
@param ArrayList<Comment> comments
    @param Object loggedUser

    @template.layout(
    content = @`
    <h2>Comments</h2>
    @if (comments.isEmpty())
    <div class="flex flex-wrap">
        <h4 class="text-blue-500">$unsafe{"No comments here ! :-("}</h4>
    </div>
    <br>
    @else
    @for(Comment c: comments)

    <div class="">
        <h4 class="text-blue-600">Comment from user #${c.user_id}</h4><br>
        <div class="flex">
            <div class="flex-1 text-gray-600">${"Posted on " + c.date}</div>
            <div class="text-gray-800 flex">
                @for (int i = 0; i < c.note; i++) <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                    fill="currentColor" class="w-6 h-6 text-blue-600">
                    <path fill-rule="evenodd"
                        d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z"
                        clip-rule="evenodd" />
                    </svg>

                    @endfor
            </div>
        </div>
        <div class="text-gray-800 my-2">${c.content}</div>
        <button v-on:click="showAnswerBox(c)" class='primaryContained float-right' type="submit">Answer</button>
    </div>

    @endfor
    @endif

    <div class="mt-10 bg-slate-200 rounded-md p-3">
        <h3 type="text">Leave a comment</h3>
        <textarea type="text" class="w-full p-1" placeholder="Write a comment" v-model="newItem"
            @keyup.enter="addItem()"></textarea>
        <br>
        <button v-on:click="addItem()" class='' type="submit">Send</button>
    </div>

    `,
    loggedUser
    )