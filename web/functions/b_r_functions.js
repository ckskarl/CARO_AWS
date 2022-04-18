
function backupButton() {
    document.querySelector('.backup-bg').style.visibility = 'visible';
    document.querySelector('.user').style.opacity = '0.3';
    document.querySelector('.userlist').style.opacity = '0.3';

}


function restoreButton() {
    document.querySelector('.restore-bg').style.visibility = 'visible';
    document.querySelector('.user').style.opacity = '0.3';
    document.querySelector('.userlist').style.opacity = '0.3';

}


function clearArchiveButton() {
    document.querySelector('.clearArchive-bg').style.visibility = 'visible';
    document.querySelector('.user').style.opacity = '0.3';
    document.querySelector('.userlist').style.opacity = '0.3';

}

function backupConfirm() {
    var javaScriptMessage = confirm("Are you sure you want to backup?");
    if (javaScriptMessage == false) {
        this.event.preventDefault();
    }
}
function restoreConfirm() {
    var javaScriptMessage = confirm("Are you sure you want to restore?");
    if (javaScriptMessage == false) {
        this.event.preventDefault();
    }
}

function cancelForm(){
    this.event.preventDefault();
}
