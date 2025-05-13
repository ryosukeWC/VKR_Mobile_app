from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import os
from dotenv import load_dotenv

load_dotenv('test.env')  # Явно указываем файл .env

# Получаем порт, если не указан - используем 5432 по умолчанию
postgres_port = os.getenv('POSTGRES_PORT', '5432')

DATABASE_URL = f"postgresql://{os.getenv('POSTGRES_USER')}:{os.getenv('POSTGRES_PASSWORD')}@{os.getenv('POSTGRES_SERVER')}:{postgres_port}/{os.getenv('POSTGRES_DB')}"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def check_db_connection():
    try:
        connection = engine.connect()
        connection.close()
        return {"status": "success", "message": "Database connection successful"}
    except Exception as e:
        return {"status": "error", "message": f"Database connection failed: {str(e)}"}