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

// Search guest
const search = () => {
    let query = $("#search-input").val();

    if (query == '') {
        $("#search-result").hide();
    } else {
        let url = `http://localhost:8282/owner/search/${query}`;
        fetch(url).then((response) => {
            // console.log(response);
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

// Get payment details by their Id
function paymentDetails(id) {
    let url = `http://localhost:8282/owner/payment/${id}`;
    fetch(url).then((response) => {
        return response.json();
    }).then((data) => {
        console.log(data);
        if (data != null) {
            $("#gateway-name").text(`${data.gateway}`);
            $("#p-amount").text(`${data.amount}`);
            $("#p-date").text(`${data.date}`);
            $("#p-refno").text(`${data.refNo}`);
            $("#p-status").text(`${data.status}`);
        } else {

        }
    });
}

const floordd = document.getElementById("floor");
const flatdd = document.getElementById("flat");
const roomdd = document.getElementById("room");

$('#warn-message2').hide()
$('#warn-message1').hide()

function getFlats(floorId) {
    let url = `http://localhost:8282/owner/floor/${floorId}`;
    fetch(url).then((response) => {
        return response.json();
    }).then((data) => {
        console.log(data);
        if (data != 0) {
            // It will remove all childNode
            while (flatdd.firstChild) {
                flatdd.removeChild(flatdd.firstChild);
            }

            data.forEach((flat) => {
                if (flat != null) {
                    console.log(flat);
                    $('#flat').show();
                    $('#warn-message1').hide();
                    let option = document.createElement("option");
                    option.value = flat["id"];
                    option.text = flat["name"]
                    //let optionText = document.createTextNode(data[flt]);
                    // option.appendChild(optionText);
                    flatdd.appendChild(option);
                } 
            });
            getRooms(flatdd.firstChild.value)
        } else {
            $('#flat').hide();
            $('#warn-message1').show()
            $('#room').hide();
            $('#warn-message2').hide()
        }
    });
}

function getRooms(flatId) {
    if (flatId != null) {
        let url2 = `http://localhost:8282/owner/flat/${flatId}`;
        try {
            fetch(url2).then((response) => {
                return response.json();
            }).then((data) => {
                console.log(data);
                if (data != 0) {
                    // It will remove all childNode
                    while (roomdd.firstChild) {
                        roomdd.removeChild(roomdd.firstChild);
                    }

                    data.forEach((room) => {
                        if (room != null) {
                            console.log(room);
                            $('#room').show();
                            $('#warn-message2').hide();
                            let option = document.createElement("option");
                            option.value = room["id"];
                            option.text = room["name"]
                            roomdd.appendChild(option);
                        }
                    });
                } else {
                    $('#room').hide();
                    $('#warn-message2').show()
                    $('#submit-btn').find('button[type=submit]').prop('disabled', true);
                    // $('#submit-btn').prop('disabled',true);
                }
            });
        } catch (error) {
            console.log(error);
        }
    } else {
        $('#submit-btn').hide();
        swal("No flats are avialiable, Add first");
    }


}

$('#allocationDetails').on('submit', function (event) {
    event.preventDefault();
    let form = new FormData(this);
    $.ajax({
        // url : 'http://localhost:8282/owner/allocate/room/guest',
        url: 'http://localhost:8282/owner/allocate/room/guest',
        type: 'PUT',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)

            if (data.trim() == 'done') {
                // swal("Saved successfully")
                //     .then((value) => {
                //         window.location = ""
                //     });
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});

//Add new pg
$('#pgdetails').on('submit', function (event) {
    event.preventDefault();
    let form = new FormData(this);
    $.ajax({
        url: $(this).attr('action'),
        type: 'POST',
        data: form,
        success: function(data, textStatus, jqXHR) {
            console.log(data);
            if(data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal(data);
            }
        }
    })
})

// document.getElementById('allocate_btn').onclick = function () {
//     var floorId = $("#floor").val();
//     var flatId = $("#flat").val();
//     var roomId = $("#room").val();
//     console.log("Floor Id : " + floorId + ", flat Id : " + flatId + ", room Id : " + roomId);
//     if (floorId !=null && flatId != null && roomId !=null ) {

//     }
// }

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