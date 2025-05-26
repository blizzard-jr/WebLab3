function submitForm(event) {
    event.preventDefault();

    // Получение значений формы
    let x = document.getElementById('x').value.trim();
    if (!isValidX(x)) {
        showToastError('Значение X должно быть числом от -3 до 5.');
        return;
    }
    let y = document.querySelector('input[name="y"]:checked');
    if (!y) {
        showToastError('Пожалуйста, выберите значение для Y.');
        return;
    }
    else{
        y = y.value;
    }
    let r = document.querySelector('input[name="r"]:checked');
    if (!r) {
        showToastError('Пожалуйста, выберите значение для R.');
        return;
    }
    else{
        r = r.value
    }
    const formData = new URLSearchParams();

    formData.append('action', 'pointsData');
    formData.append('x', x);
    formData.append('y', y);
    formData.append('r', r);


    fetch('${pageContext.request.contextPath}/controller', {
        method: 'POST',
        body: formData,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const newRow = `
                    <tr>
                        <td>${sessionScope.x}</td>
                        <td>${sessionScope.y}</td>
                        <td>${sessionScope.r}</td>
                        <td>${sessionScope.result}</td>
                        <td>${sessionScope.executionTime}</td>
                        <td>${sessionScope.currentTime} ms</td>
                    </tr>
                `;

            $('#result tbody').prepend(newRow);
            // Если статус ответа 200, перенаправляем пользователя
            window.location.href = '${pageContext.request.contextPath}/result.jsp';
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

// Проверка X (должно быть числом от -3 до 5)
function isValidX(value) {
    const x = parseFloat(value);
    return !isNaN(x) && x >= -3 && x <= 5;
}


function showToastError(message) {
    toastr.error(message, 'Ошибка', {timeOut: 5000});
}

// Показать сообщение об успехе через Toastr
function showToastSuccess(message) {
    toastr.success(message, 'Успех', {timeOut: 3000});
}