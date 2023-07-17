
// floor id
let flatId = null;
let flrId = null;
let roomId = null;

//get floor's id
function getId(id) {
    flrId = id;
}

//get flat's id
function getFlat(flatid) {
    flatId = flatid;
    $('#addroom-form').append(`<input type="number" name="flat" value="${flatId}" hidden>`);
    console.log('Flat id is ' + flatId);
}


// Get a floor by their id
function updateFloor(floorid) {
    flrId = floorid;
    // var room = null;
    if (flrId != null) {
        let url = `http://localhost:8282/owner/pg/floor/${flrId}`;
        try {
            fetch(url).then((response) => {
                return response.json();
            }).then((data) => {
                console.log(data);
                $('#floorName').text(data.name);
                $('#floorAdded-date').text(`${data.addedDate}`);
                //document.getElementById('room_name').value = data.name;
                $('#floor_name').val(data.name);
                $('#isactive').val(`${data.status}`);
                $('#editfloor-form').append(`<input type="number" name="id" value="${flrId}" hidden>`);
                // $('#editfloor-form').append(`<input type="text" name="pgDetails" value="${pgId}" hidden>`);
            });
        } catch (error) {
            console.log(error);
        }
    }
}


//update a floor by their id
$('#editfloor-form').on('submit', function (event) {
    event.preventDefault();

    let form = new FormData(this);
    $.ajax({
        url: `http://localhost:8282/owner/pg/update/floor/${flrId}`,
        type: 'PUT',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)
            if (data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal.fire(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal.fire("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});


// Get flat by their Id
function updateFlat(floorid,flatid) {
    flrId = floorid;
    flatId = flatid;
    console.log('Flat Id is ' + flatId);
    if (flatId != null) {
        let url = `http://localhost:8282/owner/pg/flat/${flatid}`;
        try {
            fetch(url).then((response) => {
                return response.json();
            }).then((data) => {
                console.log(data);
                $('#flatAdded-date').text(`${data.addedDate}`);
                //document.getElementById('room_name').value = data.name;
                $('#flat_name').val(data.name);
                $('#flatName').text(`${data.name}`);
                $('#hallSpace').val(`${data.hallSpace}`);
                $('#isactive').val(`${data.status}`);
                $('#editflat-form').append(`<input type="number" name="id" value="${flatId}" hidden>`);
                $('#editflat-form').append(`<input type="number" name="floor" value="${flrId}" hidden>`);
            });
        } catch (error) {
            console.log(error);
        }
    }
}


// Add flat in a floor
$('#addflatform').on('submit', function (event) {
    event.preventDefault();
    // it will create new input element in form for floor id
    $('#addflatform').append(`<input type="text" name="floor" value="${flrId}" hidden/>`);

    let form = new FormData(this);
    $.ajax({
        url: 'http://localhost:8282/owner/pg/add/flat',
        type: 'POST',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)

            if (data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal.fire(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal.fire("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});


//update a flat by their id
$('#editflat-form').on('submit', function (event) {
    event.preventDefault();

    let form = new FormData(this);
    $.ajax({
        url: `http://localhost:8282/owner/pg/update/flat/${flatId}`,
        type: 'PUT',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)
            if (data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal.fire(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal.fire("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});


// Get a room by their id
function updateRoom(flatid, roomid) {
    roomId = roomid;
    flatId = flatid;
    // var room = null;
    if (roomId != null) {
        let url = `http://localhost:8282/owner/pg/room/${roomid}`;
        try {
            fetch(url).then((response) => {
                return response.json();
            }).then((data) => {
                console.log(data);
                $('#alert-message').hide();
                $('#roomAdded-date').text(`${data.addedDate}`);
                //document.getElementById('room_name').value = data.name;
                $('#room_name').val(data.name);
                $('#bed_count').val(data.bedCount);
                $('#room_washroom').val(data.washRoomType);
                $('#roomName').text(`${data.name}`);
                $('#isactive').val(`${data.status}`);
                $('#editroom-form').append(`<input type="number" name="id" value="${roomId}" hidden>`);
                $('#editroom-form').append(`<input type="number" name="flat" value="${flatId}" hidden>`);
            });
        } catch (error) {
            console.log(error);
        }
    }
}

function alertMessage() {
    $('#alert-message').show();
}

// Add room in a flat
$('#addroom-form').on('submit', function (event) {
    event.preventDefault();
    // it will create new input element in form for floor id
    // $('#addflatform').append(`<input type="text" name="floor" value="${flrId}" hidden>`);

    let form = new FormData(this);
    $.ajax({
        url: 'http://localhost:8282/owner/pg/add/room',
        type: 'POST',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)

            if (data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal.fire(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal.fire("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});


//update a room by their id
$('#editroom-form').on('submit', function (event) {
    event.preventDefault();

    let form = new FormData(this);
    $.ajax({
        url: `http://localhost:8282/owner/pg/update/room/${roomId}`,
        type: 'PUT',
        data: form,

        success: function (data, textStatus, jqXHR) {
            console.log(data)
            if (data.trim() == 'done') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Saved successfully',
                }).then((value) => {
                    window.location = ""
                });
            } else {
                swal.fire(data);
            }
        },

        error: function (jqXHR, textStatus, errorThrown) {
            swal.fire("Something went wrong !");
        },
        processData: false,
        contentType: false
    });
});


function deleteRoom(roomid) {
    // this.roomId = roomid;
}


function deleteFloor(fid) {
    let form = new FormData();
    try {
        swal.fire({
            title: "Are you sure?",
            text: "You want to delete this floor!",
            icon: "warning",
            showConfirmButton: true,
            showDenyButton: true,
            confirmButtonText: 'Yes',
            dangerMode: true,
        }).then((result) => {
            if (result.isConfirmed) {
                form.append('floorId',fid);
                $.ajax({
                    url: 'http://localhost:8282/owner/pg/delete/floor',
                    type: 'DELETE',
                    data: form,
                    processData: false,
                    contentType: false,
    
                    success: function (data, textStatus, jqXHR) {
                        console.log(data);
    
                        if (data.trim() == 'done') {
                            Swal.fire({
                                icon: 'success',
                                title: 'Success',
                                text: 'Deleted successfully',
                            }).then((value) => {
                                window.location = "";
                            });
                        } else {
                            swal.fire(data);
                        }
                    }
                })
            } else if (result.isDenied) {
                Swal.fire('Changes are not saved', '', 'info')
            }
        });
    } catch (error) {
        console.log(error);
    }
}
