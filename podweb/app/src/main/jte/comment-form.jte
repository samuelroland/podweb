@param Boolean respond = false

!{var css = respond ? "mt-2 ml-4" : "mt-10";}

<form class="${css} bg-slate-200 rounded-md p-3" method="post">
    <h4 type="text">${respond ? "Respond to comment" : "Leave a comment"}</h4>
    <textarea type="text" class="w-full p-1" placeholder="Write a comment" v-model="newItem"
        @keyup.enter="addItem()"></textarea>
    <br>
    <button v-on:click="addItem()" class='' type="submit">Send</button>
</form>
