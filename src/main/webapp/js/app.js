
function isValidOption(op) {
    return op.substring(0, 2) != '--';

}

function getWorkers() {
    var e = document.getElementById("select_vin");
    var vin = e.options[e.selectedIndex].text;
    var e_date = document.getElementById("app_date");
    var date = e_date.value;
    var e_time = document.getElementById("app_time");
    var time = e_time.options[e_time.selectedIndex].text;
    if (!isValidOption(vin) || !isValidOption(time) || date == null || date == "") {
        return;
    }
    $.ajax({
        type:"POST",
        url:"get_workers.php",
        data:{vin:vin, date:date, time:time}
    }).done(function (result) {
            var data_array = $.parseJSON(result);
            e = document.getElementById("select_worker");
            if (data_array.length == 0) {
                bootbox.alert("No workers available for the vehicle model at the given time. " +
                    "Please select a different time.");
                e.options[0].selected = true;
                return;
            }
            e = document.getElementById("select_worker");
            var count = $('#select_worker option').size();
            // remove all except 1st element -- Worker --
            for (i = 1; i < count; i++) {
                e.remove(1);
            }

            for (i = 0; i < data_array.length; i++) {
                var option = document.createElement("option");
                option.text = data_array[i];
                e.add(option, null);
            }
            e.options[0].selected = true;
            e.disabled = false;
        });
}