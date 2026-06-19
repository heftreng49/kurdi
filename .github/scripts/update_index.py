#!/usr/bin/env python3
"""
Kurdî Projesi — Otomatik index.json Oluşturucu
------------------------------------------------
data/units/ klasöründeki tüm uXX_*.json dosyalarını tarar,
metadata'larını okur ve data/index.json'u yeniden oluşturur.
Versiyon numarasını da otomatik artırır (patch: 1.0.0 → 1.0.1).

Çalıştırma: python3 .github/scripts/update_index.py
"""

import json
import os
import re
from datetime import date
from pathlib import Path

# ── Yollar ───────────────────────────────────────────────────────────────────
REPO_ROOT  = Path(__file__).parent.parent.parent
DATA_DIR   = REPO_ROOT / "data"
UNITS_DIR  = DATA_DIR / "units"
INDEX_FILE = DATA_DIR / "index.json"

CDN_PRIMARY  = "https://raw.githubusercontent.com/heftreng49/kurdi/main/data/units/"
CDN_FALLBACK = "https://heftreng49.github.io/kurdi/data/units/"

# ── Mevcut versiyonu oku ve patch'i artır ────────────────────────────────────
def versiyon_artir(mevcut: str) -> str:
    """'1.0.3' → '1.0.4'"""
    try:
        major, minor, patch = mevcut.split(".")
        return f"{major}.{minor}.{int(patch) + 1}"
    except Exception:
        return "1.0.1"

# ── Mevcut index.json'u oku ──────────────────────────────────────────────────
if INDEX_FILE.exists():
    with open(INDEX_FILE, "r", encoding="utf-8") as f:
        mevcut_index = json.load(f)
    eski_versiyon = mevcut_index.get("versiyon", "1.0.0")
    eski_uniteler = {u["dosya"]: u for u in mevcut_index.get("units", [])}
else:
    eski_versiyon = "1.0.0"
    eski_uniteler = {}

# ── data/units/ içindeki tüm dosyaları tara ──────────────────────────────────
unit_dosyalari = sorted(UNITS_DIR.glob("u*.json"))

if not unit_dosyalari:
    print("⚠️  data/units/ içinde hiç ünite dosyası bulunamadı.")
    exit(0)

yeni_uniteler = []
degisiklik_var = False

for dosya_yolu in unit_dosyalari:
    dosya_adi = dosya_yolu.name

    with open(dosya_yolu, "r", encoding="utf-8") as f:
        unit_data = json.load(f)

    # Ünite dosyasından metadata çıkar
    unit_id      = unit_data.get("unit_id", dosya_adi.replace(".json", ""))
    unit_order   = unit_data.get("unit_order", 99)
    unit_title   = unit_data.get("unit_title", {"kurmanci": unit_id, "sorani": unit_id})
    kelime_sayisi = len(unit_data.get("vocabulary", []))
    soru_sayisi   = len(unit_data.get("questions", []))

    # İcon: mevcut index'ten koru, yoksa varsayılan
    eski_unit = eski_uniteler.get(dosya_adi, {})
    icon = eski_unit.get("icon", "📖")

    yeni_meta = {
        "unit_id":       unit_id,
        "unit_order":    unit_order,
        "unit_title":    unit_title,
        "icon":          icon,
        "kelime_sayisi": kelime_sayisi,
        "soru_sayisi":   soru_sayisi,
        "dosya":         dosya_adi
    }

    # Değişiklik kontrolü
    if dosya_adi not in eski_uniteler:
        print(f"➕ Yeni ünite bulundu: {dosya_adi}")
        degisiklik_var = True
    elif (eski_uniteler[dosya_adi].get("kelime_sayisi") != kelime_sayisi or
          eski_uniteler[dosya_adi].get("soru_sayisi") != soru_sayisi):
        print(f"✏️  Güncellendi: {dosya_adi} "
              f"({eski_uniteler[dosya_adi].get('kelime_sayisi')}→{kelime_sayisi} kelime, "
              f"{eski_uniteler[dosya_adi].get('soru_sayisi')}→{soru_sayisi} soru)")
        degisiklik_var = True

    yeni_uniteler.append(yeni_meta)

# Silinen ünite kontrolü
for eski_dosya in eski_uniteler:
    if not any(u["dosya"] == eski_dosya for u in yeni_uniteler):
        print(f"🗑️  Silindi: {eski_dosya}")
        degisiklik_var = True

# Sıralamaya göre düzenle
yeni_uniteler.sort(key=lambda u: u["unit_order"])

# ── Değişiklik yoksa çık ─────────────────────────────────────────────────────
if not degisiklik_var and len(yeni_uniteler) == len(eski_uniteler):
    print("✅ index.json güncel, değişiklik yok.")
    exit(0)

# ── Yeni versiyon oluştur ─────────────────────────────────────────────────────
yeni_versiyon = versiyon_artir(eski_versiyon)
bugun = date.today().isoformat()

yeni_index = {
    "versiyon":          yeni_versiyon,
    "son_guncelleme":    bugun,
    "cdn_base_primary":  CDN_PRIMARY,
    "cdn_base_fallback": CDN_FALLBACK,
    "units":             yeni_uniteler
}

# ── index.json'u yaz ─────────────────────────────────────────────────────────
with open(INDEX_FILE, "w", encoding="utf-8") as f:
    json.dump(yeni_index, f, ensure_ascii=False, indent=2)
    f.write("\n")

print(f"✅ index.json güncellendi: v{eski_versiyon} → v{yeni_versiyon}")
print(f"   Toplam ünite: {len(yeni_uniteler)}")
for u in yeni_uniteler:
    print(f"   [{u['unit_order']}] {u['unit_id']} — {u['kelime_sayisi']} kelime, {u['soru_sayisi']} soru")
