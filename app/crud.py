from datetime import datetime

from sqlalchemy.orm import Session
from . import models, schemas
from .models import DBReservation


def get_restaurants(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Restaurant).offset(skip).limit(limit).all()

def get_restaurant(db: Session, restaurant_id: int):
    return db.query(models.Restaurant).filter(models.Restaurant.restaurant_id == restaurant_id).first()


def create_reservation(db: Session, reservation_data: dict):
    # Преобразуем время из строки "HH:MM" в datetime
    time_obj = datetime.strptime(reservation_data["reservation_time"], "%H:%M").time()
    reservation_datetime = datetime.combine(
        reservation_data["reservation_date"],
        time_obj
    )

    # Создаем запись в БД
    db_reservation = DBReservation(
        user_id=reservation_data["user_id"],  # Берем user_id напрямую из запроса
        restaurant_id=reservation_data["restaurant_id"],
        reservation_date=reservation_data["reservation_date"],
        reservation_time=reservation_datetime,
        guests=reservation_data["guests"]
    )

    db.add(db_reservation)
    db.commit()
    db.refresh(db_reservation)
    return db_reservation