<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <title>Title</title>
</head>
<body>
<div class="col-12 grid-margin">
    <table id="table" class="table table-bordered">
        <thead>
        <tr>
            <th colspan="${chars?size + 1}"><input required id="in-form" type="text"></th>
        </tr>
        <tr>
            <th></th>
            <#list chars as char>
                <th>${char}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list numbers as number>
                <tr>
                    <th>${number}</th>
                    <#list chars as char>
                        <th id="${char}${number}"><input id="${char}${number}" type="hidden"></th>
                    </#list>
                </tr>
            </#list>
        </tbody>
    </table>
</div>
</body>
<script type="text/javascript">
    fetch('table', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response =>{
        if(response.ok)
            response.json().then(function(result) {
                for(res of result) {
                    document.getElementById(res.name).innerHTML = res.value;
                    document.getElementsByName(res.name).innerHTML = res.formula;
                }
            });
    });

    let cell = null;
    const re = new RegExp("([\\(\\)\\+\\-\\*\\/\\d]+)");
    console.log();
 
    document.addEventListener("click", async function (event) {
        if (event.target.getAttribute('id') !== "in-form") {
            cell = event.target.getAttribute('id');
            if (cell !== null){
                document.getElementById("in-form").focus();
                document.getElementById("in-form").value = document.getElementsByName(cell).innerHTML !== undefined?document.getElementsByName(cell).innerHTML:"";
                document.getElementById(cell).classList.add("table-primary");
            }
        }
    });

    document.addEventListener("keypress", async function (event) {
        if (event.keyCode == 13) {

            let inElem = document.getElementById("in-form")
            let str = inElem.value;
            if(re.exec(str) !== null && str.indexOf("=") === 0) {
                if (cell !== null) {
                    let body = {
                        cell: cell,
                        formula: str
                    };

                    await fetch('cells', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body : JSON.stringify(body)
                    });

                    await fetch('table', {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(response =>{
                        if(response.ok)
                            response.json().then(function(result) {
                                for(res of result) {
                                    document.getElementById(res.name).innerHTML = res.value;
                                    document.getElementsByName(res.name).innerHTML = res.formula;
                                }
                            });
                    });

                    document.getElementById(cell).innerHTML = str;
                }
                inElem.value = "";
                inElem.blur();
                document.getElementById(cell).classList.remove("table-primary");
            } else {
                alert("err");
            }
            
        }
    });
</script>
</html>