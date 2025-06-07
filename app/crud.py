from datetime import datetime

from sqlalchemy.orm import Session
from . import models
from .models import User, ReservationTable, Reservation

def get_restaurants(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Restaurant).offset(skip).limit(limit).all()

def get_restaurant(db: Session, restaurant_id: int):
    return db.query(models.Restaurant).filter(models.Restaurant.restaurant_id == restaurant_id).first()

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.user_email == email).first()


def get_reservations_by_email(db: Session, email: str):
    user = get_user_by_email(db, email)
    if not user:
        return None

    return (
        db.query(Reservation)
        .filter(Reservation.user_id == user.user_id)
        .order_by(
            Reservation.reservation_date.desc(),
            Reservation.reservation_time.desc()
        )
        .all()
    )


def create_reservation(db: Session, reservation_data):
    # Находим пользователя по email
    user = get_user_by_email(db, reservation_data.user_email)
    if not user:
        raise ValueError("User not found")

    # Преобразуем время
    time_obj = datetime.strptime(reservation_data.reservation_time, "%H:%M").time()
    reservation_datetime = datetime.combine(
        reservation_data.reservation_date,
        time_obj
    )

    # Создаем бронирование
    reservation = Reservation(
        user_id=user.user_id,  # Используем найденный user_id
        restaurant_id=reservation_data.restaurant_id,
        reservation_date=reservation_data.reservation_date,
        reservation_time=reservation_datetime,
        guests=reservation_data.guests
    )

    db.add(reservation)
    db.commit()
    db.refresh(reservation)

    # Привязываем к столу №1

    tables = ReservationTable(
        reservation_id=reservation.reservation_id,
        table_id=1
    )

    db.add(tables)
    db.commit()

    return reservation
