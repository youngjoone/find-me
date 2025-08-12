from sqlalchemy import Column, Integer, String, DateTime, ForeignKey, JSON
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func

Base = declarative_base()

class Answer(Base):
    __tablename__ = "answers"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, index=True)
    qid = Column(String, index=True)
    value = Column(Integer, nullable=True) # For objective answers
    text = Column(String, nullable=True) # For free text answers
    ts = Column(DateTime(timezone=True), server_default=func.now())

class Trait(Base):
    __tablename__ = "traits"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, unique=True, index=True)
    big5_json = Column(JSON, nullable=True)
    subtraits_json = Column(JSON, nullable=True)
    confidence_json = Column(JSON, nullable=True)
    updated_at = Column(DateTime(timezone=True), onupdate=func.now(), server_default=func.now())

class Persona(Base):
    __tablename__ = "persona"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, unique=True, index=True)
    tags_json = Column(JSON, nullable=True)
    summary_text = Column(String, nullable=True)
    version = Column(String, nullable=True)
    updated_at = Column(DateTime(timezone=True), onupdate=func.now(), server_default=func.now())
