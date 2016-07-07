/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function initDropdownList() {
    $.ajax({
        type: "Post",
        url: "../LeaveRequestOption",
        dataType: 'json',
        success: function (response) {
            var leaveReason = response.leaveReason;
            var leaveType = response.leaveType;
            dropDownListInit(leaveType, ".leaveType");
            dropDownListInit(leaveReason, ".leaveReason");
        }
    });
}

function dropDownListInit(array, target) {
    for (var i = 0, j = array.length; i < j; i++) {
        var obj = array[i];
        $(target).append("<option value=" + obj.id + ">" + obj.name + "</option>");
    }
}

function dulpicateDropDownList(obj, selection) {
    var object = obj.clone(false);
    object.find("option[value='" + selection + "']").attr("selected", "selected");
    return object.prop('outerHTML');
}

function dulpicateDropDownListByVal(obj, selection) {
    var object = obj.clone(false);
    object.val(selection);
    return object.prop('outerHTML');
}