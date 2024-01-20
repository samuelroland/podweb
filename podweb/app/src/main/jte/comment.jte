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

    <div class="flex flex-wrap">
        <h4 class="text-blue-600">$unsafe{"Comment from user #" + c.user_id}</h4><br>
        <h6 class="text-gray-600">$unsafe{"Posted on" + c.date}</h6>
        <h5 class="text-gray-800">$unsafe{c.content}</h5>
        <h5 class="text-gray-800">$unsafe{"Note : " + c.note}</h5>
        <button v-on:click="showAnswerBox(c)" class='primaryContained float-right' type="submit">Answer to this comment</button>
    </div>
    </div>

    @endfor
    @endif

    <div class="row">
        <p type="text">Leave a comment:</p><br>
        <textarea type="text" class="input" placeholder="Write a comment" v-model="newItem"
            @keyup.enter="addItem()"></textarea>
        <button v-on:click="addItem()" class='primaryContained float-right' type="submit">Send comment</button>
    </div>

    `,
    loggedUser
    )

<script>
    export default {
        data() {
            return {
                showAnswerBox: false,
                answer: ''
            }
        },
        methods: {
            showAnswerBox(c) {
                this.showAnswerBox = true;
                this.answer = '';
            },
            addAnswer() {
                // Add the answer to the database or perform any other action
                this.showAnswerBox = false;
            }
        }
    }
</script>