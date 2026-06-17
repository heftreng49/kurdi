# Kurdî — Çift Yönlü Kürtçe Öğrenme Uygulaması

Kurmancî ↔ Soranî paralel eğitim platformu.  
Sıfır sunucu maliyeti — tamamen GitHub altyapısı üzerinde çalışır.

## Kurulum

```bash
git clone https://github.com/heftreng49/kurdi.git
cd kurdi
chmod +x gradlew
./gradlew assembleDebug
```

## JSON CDN

```
Birincil: https://raw.githubusercontent.com/heftreng49/kurdi/main/data/index.json
Yedek:    https://heftreng49.github.io/kurdi/data/index.json
```

## Yeni Ünite Ekleme

1. `data/units/uXX_isim.json` dosyasını oluştur
2. `data/index.json`'a meta kaydını ekle
3. `git push` → GitHub Actions otomatik deploy eder
