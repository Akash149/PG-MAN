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

// Search guest
const search = () => {
    let query = $("#search-input").val();

    if(query == '') {
        $("#search-result").hide();
    } else {
        let url = `http://localhost:8282/owner/search/${query}`;
        fetch(url).then((response)=> {
            return response.json();
        }).then((data)=> {
            let text = `<div class='list-group'>`
            data.forEach((guest) => {
                text+=`<a href='/owner/${guest.getPgDetails.getId()}/${guest.getId()}/' class='list-group-item lit-group-item-action'>.${guest.name}</a>`
            });
            text+=`</div>`;

            $("#search-result").html(text);
            $("#search-result").show();
        });
        $("#search-result").show();
    }
}