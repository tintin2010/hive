/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#include "complex_types.h"

#include <algorithm>



const char* PropValueUnion::ascii_fingerprint = "123CD9D82D5B5054B5054EFD63FC8590";
const uint8_t PropValueUnion::binary_fingerprint[16] = {0x12,0x3C,0xD9,0xD8,0x2D,0x5B,0x50,0x54,0xB5,0x05,0x4E,0xFD,0x63,0xFC,0x85,0x90};

uint32_t PropValueUnion::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->intValue);
          this->__isset.intValue = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_I64) {
          xfer += iprot->readI64(this->longValue);
          this->__isset.longValue = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->stringValue);
          this->__isset.stringValue = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_DOUBLE) {
          xfer += iprot->readDouble(this->doubleValue);
          this->__isset.doubleValue = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->flag);
          this->__isset.flag = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->lString.clear();
            uint32_t _size0;
            ::apache::thrift::protocol::TType _etype3;
            xfer += iprot->readListBegin(_etype3, _size0);
            this->lString.resize(_size0);
            uint32_t _i4;
            for (_i4 = 0; _i4 < _size0; ++_i4)
            {
              xfer += iprot->readString(this->lString[_i4]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.lString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->unionMStringString.clear();
            uint32_t _size5;
            ::apache::thrift::protocol::TType _ktype6;
            ::apache::thrift::protocol::TType _vtype7;
            xfer += iprot->readMapBegin(_ktype6, _vtype7, _size5);
            uint32_t _i9;
            for (_i9 = 0; _i9 < _size5; ++_i9)
            {
              std::string _key10;
              xfer += iprot->readString(_key10);
              std::string& _val11 = this->unionMStringString[_key10];
              xfer += iprot->readString(_val11);
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.unionMStringString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t PropValueUnion::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("PropValueUnion");

  if (this->__isset.intValue) {
    xfer += oprot->writeFieldBegin("intValue", ::apache::thrift::protocol::T_I32, 1);
    xfer += oprot->writeI32(this->intValue);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.longValue) {
    xfer += oprot->writeFieldBegin("longValue", ::apache::thrift::protocol::T_I64, 2);
    xfer += oprot->writeI64(this->longValue);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.stringValue) {
    xfer += oprot->writeFieldBegin("stringValue", ::apache::thrift::protocol::T_STRING, 3);
    xfer += oprot->writeString(this->stringValue);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.doubleValue) {
    xfer += oprot->writeFieldBegin("doubleValue", ::apache::thrift::protocol::T_DOUBLE, 4);
    xfer += oprot->writeDouble(this->doubleValue);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.flag) {
    xfer += oprot->writeFieldBegin("flag", ::apache::thrift::protocol::T_BOOL, 5);
    xfer += oprot->writeBool(this->flag);
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldBegin("lString", ::apache::thrift::protocol::T_LIST, 6);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->lString.size()));
    std::vector<std::string> ::const_iterator _iter12;
    for (_iter12 = this->lString.begin(); _iter12 != this->lString.end(); ++_iter12)
    {
      xfer += oprot->writeString((*_iter12));
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("unionMStringString", ::apache::thrift::protocol::T_MAP, 7);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->unionMStringString.size()));
    std::map<std::string, std::string> ::const_iterator _iter13;
    for (_iter13 = this->unionMStringString.begin(); _iter13 != this->unionMStringString.end(); ++_iter13)
    {
      xfer += oprot->writeString(_iter13->first);
      xfer += oprot->writeString(_iter13->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(PropValueUnion &a, PropValueUnion &b) {
  using ::std::swap;
  swap(a.intValue, b.intValue);
  swap(a.longValue, b.longValue);
  swap(a.stringValue, b.stringValue);
  swap(a.doubleValue, b.doubleValue);
  swap(a.flag, b.flag);
  swap(a.lString, b.lString);
  swap(a.unionMStringString, b.unionMStringString);
  swap(a.__isset, b.__isset);
}

const char* IntString::ascii_fingerprint = "52C6DAB6CF51AF617111F6D3964C6503";
const uint8_t IntString::binary_fingerprint[16] = {0x52,0xC6,0xDA,0xB6,0xCF,0x51,0xAF,0x61,0x71,0x11,0xF6,0xD3,0x96,0x4C,0x65,0x03};

uint32_t IntString::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->myint);
          this->__isset.myint = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->myString);
          this->__isset.myString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->underscore_int);
          this->__isset.underscore_int = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t IntString::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("IntString");

  xfer += oprot->writeFieldBegin("myint", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32(this->myint);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("myString", ::apache::thrift::protocol::T_STRING, 2);
  xfer += oprot->writeString(this->myString);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("underscore_int", ::apache::thrift::protocol::T_I32, 3);
  xfer += oprot->writeI32(this->underscore_int);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(IntString &a, IntString &b) {
  using ::std::swap;
  swap(a.myint, b.myint);
  swap(a.myString, b.myString);
  swap(a.underscore_int, b.underscore_int);
  swap(a.__isset, b.__isset);
}

const char* Complex::ascii_fingerprint = "FFA84FEA7037F5858F2BFEDA73AD679A";
const uint8_t Complex::binary_fingerprint[16] = {0xFF,0xA8,0x4F,0xEA,0x70,0x37,0xF5,0x85,0x8F,0x2B,0xFE,0xDA,0x73,0xAD,0x67,0x9A};

uint32_t Complex::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->aint);
          this->__isset.aint = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->aString);
          this->__isset.aString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->lint.clear();
            uint32_t _size14;
            ::apache::thrift::protocol::TType _etype17;
            xfer += iprot->readListBegin(_etype17, _size14);
            this->lint.resize(_size14);
            uint32_t _i18;
            for (_i18 = 0; _i18 < _size14; ++_i18)
            {
              xfer += iprot->readI32(this->lint[_i18]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.lint = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->lString.clear();
            uint32_t _size19;
            ::apache::thrift::protocol::TType _etype22;
            xfer += iprot->readListBegin(_etype22, _size19);
            this->lString.resize(_size19);
            uint32_t _i23;
            for (_i23 = 0; _i23 < _size19; ++_i23)
            {
              xfer += iprot->readString(this->lString[_i23]);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.lString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->lintString.clear();
            uint32_t _size24;
            ::apache::thrift::protocol::TType _etype27;
            xfer += iprot->readListBegin(_etype27, _size24);
            this->lintString.resize(_size24);
            uint32_t _i28;
            for (_i28 = 0; _i28 < _size24; ++_i28)
            {
              xfer += this->lintString[_i28].read(iprot);
            }
            xfer += iprot->readListEnd();
          }
          this->__isset.lintString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->mStringString.clear();
            uint32_t _size29;
            ::apache::thrift::protocol::TType _ktype30;
            ::apache::thrift::protocol::TType _vtype31;
            xfer += iprot->readMapBegin(_ktype30, _vtype31, _size29);
            uint32_t _i33;
            for (_i33 = 0; _i33 < _size29; ++_i33)
            {
              std::string _key34;
              xfer += iprot->readString(_key34);
              std::string& _val35 = this->mStringString[_key34];
              xfer += iprot->readString(_val35);
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.mStringString = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->attributes.clear();
            uint32_t _size36;
            ::apache::thrift::protocol::TType _ktype37;
            ::apache::thrift::protocol::TType _vtype38;
            xfer += iprot->readMapBegin(_ktype37, _vtype38, _size36);
            uint32_t _i40;
            for (_i40 = 0; _i40 < _size36; ++_i40)
            {
              std::string _key41;
              xfer += iprot->readString(_key41);
              std::map<std::string, std::map<std::string, PropValueUnion> > & _val42 = this->attributes[_key41];
              {
                _val42.clear();
                uint32_t _size43;
                ::apache::thrift::protocol::TType _ktype44;
                ::apache::thrift::protocol::TType _vtype45;
                xfer += iprot->readMapBegin(_ktype44, _vtype45, _size43);
                uint32_t _i47;
                for (_i47 = 0; _i47 < _size43; ++_i47)
                {
                  std::string _key48;
                  xfer += iprot->readString(_key48);
                  std::map<std::string, PropValueUnion> & _val49 = _val42[_key48];
                  {
                    _val49.clear();
                    uint32_t _size50;
                    ::apache::thrift::protocol::TType _ktype51;
                    ::apache::thrift::protocol::TType _vtype52;
                    xfer += iprot->readMapBegin(_ktype51, _vtype52, _size50);
                    uint32_t _i54;
                    for (_i54 = 0; _i54 < _size50; ++_i54)
                    {
                      std::string _key55;
                      xfer += iprot->readString(_key55);
                      PropValueUnion& _val56 = _val49[_key55];
                      xfer += _val56.read(iprot);
                    }
                    xfer += iprot->readMapEnd();
                  }
                }
                xfer += iprot->readMapEnd();
              }
            }
            xfer += iprot->readMapEnd();
          }
          this->__isset.attributes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 8:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->unionField1.read(iprot);
          this->__isset.unionField1 = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 9:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->unionField2.read(iprot);
          this->__isset.unionField2 = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 10:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->unionField3.read(iprot);
          this->__isset.unionField3 = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Complex::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Complex");

  xfer += oprot->writeFieldBegin("aint", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32(this->aint);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("aString", ::apache::thrift::protocol::T_STRING, 2);
  xfer += oprot->writeString(this->aString);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("lint", ::apache::thrift::protocol::T_LIST, 3);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_I32, static_cast<uint32_t>(this->lint.size()));
    std::vector<int32_t> ::const_iterator _iter57;
    for (_iter57 = this->lint.begin(); _iter57 != this->lint.end(); ++_iter57)
    {
      xfer += oprot->writeI32((*_iter57));
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("lString", ::apache::thrift::protocol::T_LIST, 4);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->lString.size()));
    std::vector<std::string> ::const_iterator _iter58;
    for (_iter58 = this->lString.begin(); _iter58 != this->lString.end(); ++_iter58)
    {
      xfer += oprot->writeString((*_iter58));
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("lintString", ::apache::thrift::protocol::T_LIST, 5);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->lintString.size()));
    std::vector<IntString> ::const_iterator _iter59;
    for (_iter59 = this->lintString.begin(); _iter59 != this->lintString.end(); ++_iter59)
    {
      xfer += (*_iter59).write(oprot);
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("mStringString", ::apache::thrift::protocol::T_MAP, 6);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->mStringString.size()));
    std::map<std::string, std::string> ::const_iterator _iter60;
    for (_iter60 = this->mStringString.begin(); _iter60 != this->mStringString.end(); ++_iter60)
    {
      xfer += oprot->writeString(_iter60->first);
      xfer += oprot->writeString(_iter60->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("attributes", ::apache::thrift::protocol::T_MAP, 7);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_MAP, static_cast<uint32_t>(this->attributes.size()));
    std::map<std::string, std::map<std::string, std::map<std::string, PropValueUnion> > > ::const_iterator _iter61;
    for (_iter61 = this->attributes.begin(); _iter61 != this->attributes.end(); ++_iter61)
    {
      xfer += oprot->writeString(_iter61->first);
      {
        xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_MAP, static_cast<uint32_t>(_iter61->second.size()));
        std::map<std::string, std::map<std::string, PropValueUnion> > ::const_iterator _iter62;
        for (_iter62 = _iter61->second.begin(); _iter62 != _iter61->second.end(); ++_iter62)
        {
          xfer += oprot->writeString(_iter62->first);
          {
            xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(_iter62->second.size()));
            std::map<std::string, PropValueUnion> ::const_iterator _iter63;
            for (_iter63 = _iter62->second.begin(); _iter63 != _iter62->second.end(); ++_iter63)
            {
              xfer += oprot->writeString(_iter63->first);
              xfer += _iter63->second.write(oprot);
            }
            xfer += oprot->writeMapEnd();
          }
        }
        xfer += oprot->writeMapEnd();
      }
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("unionField1", ::apache::thrift::protocol::T_STRUCT, 8);
  xfer += this->unionField1.write(oprot);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("unionField2", ::apache::thrift::protocol::T_STRUCT, 9);
  xfer += this->unionField2.write(oprot);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("unionField3", ::apache::thrift::protocol::T_STRUCT, 10);
  xfer += this->unionField3.write(oprot);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(Complex &a, Complex &b) {
  using ::std::swap;
  swap(a.aint, b.aint);
  swap(a.aString, b.aString);
  swap(a.lint, b.lint);
  swap(a.lString, b.lString);
  swap(a.lintString, b.lintString);
  swap(a.mStringString, b.mStringString);
  swap(a.attributes, b.attributes);
  swap(a.unionField1, b.unionField1);
  swap(a.unionField2, b.unionField2);
  swap(a.unionField3, b.unionField3);
  swap(a.__isset, b.__isset);
}


