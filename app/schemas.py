from pydantic import BaseModel, field_serializer
from datetime import time, datetime, date
from typing import Optional

class RestaurantBase(BaseModel):
    restaurant_name: str
    restaurant_address: str
    restaurant_phone: str
    open_time: time  # Изменено на datetime.time
    close_time: time  # Изменено на datetime.time

class RestaurantCreate(RestaurantBase):
    pass

class Restaurant(RestaurantBase):
    restaurant_id: int
    restaurant_description: Optional[str] = None
    restaurant_logo: Optional[str] = None
    restaurant_rating: Optional[float] = None
    created_at: datetime

    # Добавляем сериализаторы для времени
    @field_serializer('open_time', 'close_time')
    def serialize_time(self, t: time, _info):
        return t.strftime("%H:%M")

    class Config:
        from_attributes = True

class ReservationCreate(BaseModel):
    user_id: int  # Теперь принимаем user_id напрямую
    restaurant_id: int
    reservation_date: date
    reservation_time: str  # Формат "HH:MM"
    guests: int

class Reservation(BaseModel):
    reservation_id: int
    user_id: int
    restaurant_id: int
    reservation_date: date
    reservation_time: datetime
    guests: int
    status: str
    created_at: datetime

    class Config:
        from_attributes = True
