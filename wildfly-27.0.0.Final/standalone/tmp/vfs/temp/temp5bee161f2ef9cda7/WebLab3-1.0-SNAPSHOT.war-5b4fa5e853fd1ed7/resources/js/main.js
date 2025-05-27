let clickedPoints = [];
const canvas = document.getElementById("graphCanvas")
const ctx = canvas.getContext("2d");
let hasUserInputR;
const baseScaling = (Math.min(canvas.width, canvas.height) / 3);
const xCenter = canvas.width / 2;
const yCenter = canvas.height / 2;


window.onload = function () {
        setCanvasDPI();
        drawGraph(3);
        drawPoint();
    }
    const rOptions = document.getElementsByName("rSpinner");
    // Функция для проверки и обработки изменения значения R
    function checkSelectedR() {
        const spinner = PF('spinnerWidget');
        if (spinner) {
            const rValue = spinner.getValue();
            if (rValue) {
                console.log(`Выбрано значение R: ${rValue}`);
                hasUserInputR = rValue;
                drawGraph(rValue);
                drawPoint();
            } else {
                console.error('Значение R не выбрано');
            }
        } else {
            console.error('Элемент spinner не найден');
        }
    }

canvas.addEventListener("click", handleClick);

function handleClick(event) {
    event.preventDefault();
    if (hasUserInputR) {
        const dpi = window.devicePixelRatio;
        let x = event.offsetX;
        let y = event.offsetY;
        x -=  xCenter;
        y -= yCenter;
        x /= baseScaling;
        y /= baseScaling;
        scaledX = (x * hasUserInputR).toFixed(3);
        scaledY = (-y * hasUserInputR).toFixed(3);

        // Используйте полный идентификатор
        document.getElementById('pointForm:hiddenX').value = scaledX;
        document.getElementById('pointForm:hiddenY').value = scaledY;
        document.getElementById('pointForm:hiddenR').value = hasUserInputR;

        // Используем стандартный submit вместо jsf.ajax.request
        const form = document.getElementById('pointForm');
        const checkButton = document.getElementById('pointForm:check');
        
        // Создаем событие клика на кнопке
        checkButton.click();
        
    } else {
        showToastError("Выберите значение r");
        return;
    }
}


    let dynamicScalingFactor;

    function setCanvasDPI() {
        let dpi = window.devicePixelRatio;
        let canvasElement = document.getElementById('graphCanvas');
        let style = {
            height() {
                return +getComputedStyle(canvasElement).getPropertyValue('height').slice(0, -2);
            },
            width() {
                return +getComputedStyle(canvasElement).getPropertyValue('width').slice(0, -2);
            }
        };

        canvasElement.setAttribute('width', style.width() * dpi);
        canvasElement.setAttribute('height', style.height() * dpi);
    }

    function drawGraph(R) {
        let width = canvas.width;
        let height = canvas.height;

        let yAxisOffset = 15;

        // Clear canvas
        ctx.clearRect(0, 0, width, height);
        ctx.font = "15px Arial";

        // Draw x and y axes
        ctx.strokeStyle = "gray";
        ctx.lineWidth = 1;
        ctx.beginPath();
        drawAxis(ctx, 0, height / 2, width, height / 2);  // X-axis
        drawAxis(ctx, width / 2, height, width / 2, 0); // Y-axis
        ctx.stroke();

        // Drawing the areas

        // Triangle (upper right)
        ctx.fillStyle = "#0000FF10"; // blue with 10% opacity
        ctx.beginPath();
        ctx.moveTo(width / 2, height / 2);
        ctx.lineTo(width / 2, height / 2 - 3 * baseScaling);
        ctx.lineTo(width / 2 + 3 * baseScaling, height / 2);
        ctx.closePath();
        ctx.fill();
        ctx.strokeStyle = "#0000FF";
        ctx.stroke();
        // Rectangle (lower right)
        ctx.fillStyle = "#FFFF0010"; // yellow with 10% opacity
        ctx.fillRect(width / 2, height / 2, 3 * baseScaling/2, 3 * baseScaling);
        ctx.strokeStyle = "#FFFF00";
        ctx.strokeRect(width / 2, height / 2, 3 * baseScaling/2, 3 * baseScaling);

        // Semi-circle (lower left)
        ctx.fillStyle = "#39FF1410"; // green with 10% opacity
        ctx.beginPath();
        ctx.arc(width / 2, height / 2, 3 * baseScaling, 0.5 * Math.PI,  Math.PI);
        ctx.lineTo(width / 2, height / 2);
        ctx.closePath();
        ctx.fill();
        ctx.strokeStyle = "#39FF14";
        ctx.beginPath();
        ctx.arc(width / 2, height / 2, 3 * baseScaling, 0.5 * Math.PI, Math.PI);
        ctx.stroke();
        // Draw labels
        ctx.fillStyle = "white";
        const labelR = hasUserInputR ? R.toString() : "R";
        const labelRHalf = hasUserInputR ? (R / 2).toString() : "R/2";

        // X-axis labels
        ctx.fillText(labelR, width / 2 + 3 * baseScaling, height / 2 + 30);
        ctx.fillText(labelRHalf, width / 2 + 3 * baseScaling/2, height / 2 + 30);
        ctx.fillText('-' + labelR, width / 2 - 3 * baseScaling, height / 2 + 30);
        ctx.fillText('-' + labelRHalf, width / 2 - 3 * baseScaling/2, height / 2 + 30);

        // Y-axis labels
        ctx.fillText(labelR, width / 2 + yAxisOffset, height / 2 - 3 * baseScaling);
        ctx.fillText(labelRHalf, width / 2 + yAxisOffset, height / 2 - 3 * baseScaling/2);
        ctx.fillText('-' + labelR, width / 2 + yAxisOffset, height / 2 + 3 * baseScaling);
        ctx.fillText('-' + labelRHalf, width / 2 + yAxisOffset, height / 2 + 3 * baseScaling/2);

        // Draw ticks
        ctx.fillStyle = "white";
        // X-axis tics
        const tickLength = 10; // Length of the tick marks
        for (let tickValue = -R; tickValue <= R; tickValue += R / 2) {
            const xTickPosition = width / 2 + tickValue * 3 * baseScaling;
            ctx.beginPath();
            ctx.moveTo(xTickPosition, height / 2 - tickLength / 2);
            ctx.lineTo(xTickPosition, height / 2 + tickLength / 2);
            ctx.stroke();
        }

        // Y-axis tics
        for (let tickValue = -R; tickValue <= R; tickValue += R / 2) {
            const yTickPosition = height / 2 - tickValue * 3 * baseScaling;
            ctx.beginPath();
            ctx.moveTo(width / 2 - tickLength / 2, yTickPosition);
            ctx.lineTo(width / 2 + tickLength / 2, yTickPosition);
            ctx.stroke();
        }
        if (hasUserInputR) {
            // console.log("drawing points");
            drawAllPoints();
        }
    }

    function drawAxis(context, fromX, fromY, toX, toY) {
        const headLength = 10;
        const angle = Math.atan2(toY - fromY, toX - fromX);

        context.beginPath();
        context.moveTo(fromX, fromY);
        context.lineTo(toX, toY);
        context.lineTo(toX - headLength * Math.cos(angle - Math.PI / 6), toY - headLength * Math.sin(angle - Math.PI / 6));
        context.moveTo(toX, toY);
        context.lineTo(toX - headLength * Math.cos(angle + Math.PI / 6), toY - headLength * Math.sin(angle + Math.PI / 6));
        context.stroke();
    }

    function drawPoint() {
        const table = document.querySelector('.result');
        if (table) {
            const observer = new MutationObserver(() => {
                updatePointsFromTable();
            });

            observer.observe(table, { childList: true, subtree: true });

            // Initial drawing
            updatePointsFromTable();
        }
    }

    function updatePointsFromTable() {
        const dpi = window.devicePixelRatio;
        const table = document.querySelector('.result');
        if (table) {
            const rows = table.querySelectorAll("tbody tr");
            clickedPoints = []; // Clear previous points
            rows.forEach(row => {
                const xElement = row.querySelector('[id$="xValue"]');
                const yElement = row.querySelector('[id$="yValue"]');
                const resElement = row.querySelector('[id$="resultValue"]');
                const rElement = row.querySelector('[id$="rValue"]')

                if (xElement && yElement && resElement && rElement) {
                    const xText = xElement.textContent ? xElement.textContent.trim() : '';
                    const yText = yElement.textContent ? yElement.textContent.trim() : '';
                    const resText = resElement.textContent ? resElement.textContent.trim() : '';
                    const rText = rElement.textContent ? rElement.textContent.trim() : '';

                    const x = parseFloat(xText);
                    const y = parseFloat(yText);
                    const r = parseFloat(rText);

                    if (!isNaN(x) && !isNaN(y) && !isNaN(r) && resText) {
                        const dpi = window.devicePixelRatio;
                        ctx.fillStyle = (resText === "true") ? "green" : "red";
                        ctx.beginPath();
                        // Используем r из данных, если hasUserInputR не определен
                        const currentR = hasUserInputR || r;
                        let scaleX = xCenter + (x / currentR) * baseScaling;
                        let scaleY = yCenter - (y / currentR) * baseScaling;
                        ctx.arc(scaleX, scaleY, 5, 0, Math.PI * 2);
                        ctx.fill();
                    } else {
                        console.warn('Некорректные данные для точки:', { x: xText, y: yText, r: rText, res: resText });
                    }
                } else {
                    console.warn('Не найдены элементы с данными в строке таблицы');
                }
            });
        } else {
            console.error('Таблица с классом .result не найдена');
        }
    }

    async function drawAllPoints() {
        clickedPoints.forEach(point => {
            ctx.fillStyle = point.c;
            ctx.beginPath();
            ctx.arc(point.x, point.y, 5, 0, Math.PI * 2);
            //ctx.fillStyle = "blue"; // Цвет точки
            ctx.fill();
        });
    }

