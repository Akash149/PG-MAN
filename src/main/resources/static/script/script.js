// alert("hello akash")
const toggleSidebar = () => {

    if($('.sidebar').is(":visible")) {
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%")
        console.info("Dashboard hide");
    } else {
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%")
    }
};

// update pgDetails form
// function updatePgDetails()