$(document).ready(function(){

    $("#buttonCancel").on("click", function(){
        window.location = moduleURL;
    });

    $("#fileImage").change(function (){
        fileSize = this.files[0].size;
        // alert("File size: " + fileSize);

        if(fileSize > MAX_FILE_SIZE){
            this.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput){

    var file = fileInput.files[0];
    var reader = new FileReader();

    reader.onload = function (e){
        $("#thumbnail").attr("src", e.target.result)
    };
    reader.readAsDataURL(file);
}

$(document).ready(function(){
    $("#backCancel").on("click", function(){
        window.location = "[[@{/users}]]";
    });

});

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal("show");

}

function showWarningModal(message){
    showModalDialog("Warning", message);
}

function showErrorModal(message){
    showModalDialog("Error", message);
}