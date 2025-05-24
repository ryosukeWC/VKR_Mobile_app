import os
from dotenv import load_dotenv
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from app.crud import get_user_by_email

# Загрузка переменных окружения
load_dotenv('test.env')  # Явно указываем файл .env

# Получаем порт, если не указан - используем 5432 по умолчанию
postgres_port = os.getenv('POSTGRES_PORT', '5432')

DATABASE_URL = f"postgresql://{os.getenv('POSTGRES_USER')}:{os.getenv('POSTGRES_PASSWORD')}@{os.getenv('POSTGRES_SERVER')}:{postgres_port}/{os.getenv('POSTGRES_DB')}"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


def test_get_user_by_email():
    # Создаем сессию
    db = SessionLocal()

    try:
        # 1. Проверяем существующего пользователя
        test_email = "killua70ld1k@gmail.com"  # Замените на реальный email из вашей БД
        user = get_user_by_email(db, test_email)

        if user:
            print(f"✅ Пользователь найден: ID={user.user_id}, Email={user.user_email}")
        else:
            print(f"❌ Пользователь с email {test_email} не найден")

        # 2. Проверяем несуществующего пользователя
        non_existent_email = "non_existent@example.com"
        user = get_user_by_email(db, non_existent_email)

        if user is None:
            print(f"✅ Корректно: пользователь {non_existent_email} не найден")
        else:
            print(f"❌ Ошибка: найден несуществующий пользователь")

    except Exception as e:
        print(f"⚠️ Ошибка при работе с базой данных: {e}")
    finally:
        db.close()


if __name__ == "__main__":
    test_get_user_by_email()