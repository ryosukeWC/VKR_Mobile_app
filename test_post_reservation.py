from fastapi.testclient import TestClient
from app.main import app  # Импортируем ваш FastAPI app
import json

client = TestClient(app)


def test_create_reservation():
    # Тестовые данные
    test_data = {
        "restaurant_id": 1,
        "user_email": "killua70ld1k@gmail.com",  # Используем тестовый email
        "reservation_date": "2023-12-31",
        "reservation_time": "19:00",
        "guests": 4
    }

    # Отправляем POST-запрос
    response = client.post(
        "/reservations/",
        json=test_data
    )

    # Проверяем результат
    assert response.status_code == 200
    assert "reservation_id" in response.json()
    print("\nТест пройден! Ответ сервера:", response.json())

if __name__ == "__main__":
    test_create_reservation()