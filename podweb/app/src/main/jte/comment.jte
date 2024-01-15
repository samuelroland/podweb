    <!-- Add icon library -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<span class="heading">User Rating</span>
<span class="fa fa-star checked"></span>
<span class="fa fa-star checked"></span>
<span class="fa fa-star checked"></span>
<span class="fa fa-star checked"></span>
<span class="fa fa-star"></span>
<p>4.1 average based on 254 reviews.</p>
<hr style="border:3px solid #f1f1f1">

<section id="com">
    <div class="row">
        <div class="side">
            <div>5 star</div>
        </div>
        <div class="middle">
            <div class="bar-container">
                <div class="bar-5"></div>
            </div>
        </div>
        <div class="side right">
            <div>150</div>
        </div>
        <div class="side">
            <div>4 star</div>
        </div>
        <div class="middle">
            <div class="bar-container">
                <div class="bar-4"></div>
            </div>
        </div>
        <div class="side right">
            <div>63</div>
        </div>
        <div class="side">
            <div>3 star</div>
        </div>
        <div class="middle">
            <div class="bar-container">
                <div class="bar-3"></div>
            </div>
        </div>
        <div class="side right">
            <div>15</div>
        </div>
        <div class="side">
            <div>2 star</div>
        </div>
        <div class="middle">
            <div class="bar-container">
                <div class="bar-2"></div>
            </div>
        </div>
        <div class="side right">
            <div>6</div>
        </div>
        <div class="side">
            <div>1 star</div>
        </div>
        <div class="middle">
            <div class="bar-container">
                <div class="bar-1"></div>
            </div>
        </div>
        <div class="side right">
            <div>20</div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-6">
                <div class="comment">
                    <p v-for="items in item" v-text="items"></p>
                </div><!--End Comment-->
            </div><!--End col -->
        </div><!-- End row -->
        <div class="row">
            <div class="col-6">
                <textarea type="text" class="input" placeholder="Write a comment" v-model="newItem" @keyup.enter="addItem()"></textarea>
                <button v-on:click="addItem()" class='primaryContained float-right' type="submit">Add Comment</button>
            </div><!-- End col -->
        </div><!--End Row -->
    </div><!--End Container -->
</section><!-- end App -->