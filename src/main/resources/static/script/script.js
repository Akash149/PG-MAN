// alert("hello akash")
const toggleSidebar = () => {

    if ($('.sidebar').is(":visible")) {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%")
        console.info("Dashboard hide");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%")
    }
};

// update pgDetails form
// function updatePgDetails()

// Search guest
const search = () => {
    let query = $("#search-input").val();

    if (query == '') {
        $("#search-result").hide();
    } else {
        let url = `http://localhost:8282/owner/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {
            console.log(data);
            let text = `<div class='list-group'>`
            data.forEach((guest) => {
                if (guest == null) {
                    text += 'Not found'
                } else {
                text += `<a href='/owner/'${guest.getPgDetails.getId()}''/'${guest.getId()}'/' class='list-group-item list-group-item-action'>.${guest.name}</a>`
            }
            });
            text += `</div>`;

            $("#search-result").html(text);
            $("#search-result").show();
        }).catch(error => {
            console.error(error);
        });
        $("#search-result").show();
    }
}

function getFloorData() {
    $("select#floors").change(function() {
        let floorId = $(this).children("option:selected").val();
        console.log("You have selected value is " + floorId);
    });
}

// $(document).ready(function() {
//     $("select.selectVal").change(function() {
//         let selecteditem = $(this).children("option:selected").val();
//         console.log("You have selected value is " + selecteditem);
//     });
// });


console.log('This is Drag and drop')
const guest = document.querySelector('.guest');
const flatView = document.getElementsByClassName('card');

// when we start to drag
guest.addEventListener('dragstart', (e) => {
    console.log("DragStart has been triggered");
    e.target.className += ' hold';
    setTimeout(() => {
        e.target.className = 'hide';
    }, 0);
});

// when we start to drop
guest.addEventListener('dragend', (e) => {
    console.log("DragStart has been ended");
    // e.target.className += ' guest'
});

for (flat of flatView) {
    flat.addEventListener('dragover', (e) => {
        e.preventDefault();
        console.log("Dragover has been triggered");
    });

    flat.addEventListener('dragenter', (e) => {
        console.log("Dragenter has been triggered");
        e.target.className += ' dashed';
    });
    flat.addEventListener('dragleave', (e) => {
        console.log("Dragleave has been triggered");
        // e.target.className = 'flat';
    });
    flat.addEventListener('drop', (e) => {
        console.log("Dragdrop has been triggered");
        e.target.append(guest);
        // e.target.className 
    });
}