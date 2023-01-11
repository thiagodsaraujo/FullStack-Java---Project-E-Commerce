$(document).ready(function(){
    $("#logoutLink").on("click", function(e){
        e.preventDefault();
        document.logoutForm.submit();
    });
    // customizeDropDownMenu();
});


function customizeDropDownMenu(){
    $("#accountLink").click(function (){
        location.href = this.href;
    });
}


function customizeDropDownMenu(){
        $
        $(".dropdown > a").click(function (){
            location.href = this.href;
        });
}