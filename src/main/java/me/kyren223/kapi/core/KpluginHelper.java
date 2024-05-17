/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.core;

import me.kyren223.kapi.math.CryptoUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.UUID;

public class KpluginHelper {
    
    private KpluginHelper() {
        throw new AssertionError("Cannot instantiate utility class");
    }
    
    public static Class<?> tryLoadingKapi(Kplugin plugin, Class<?> clazz) {
        final String name = plugin.getPluginName();
        if (clazz != null) return clazz;
        try {
            clazz = Class.forName(name + ".me.kyren223.kapi.core.KapiInit");
            System.out.println("KapiInit has been loaded!");
            return clazz;
        } catch (ClassNotFoundException e) {
            return askServerForClass(plugin);
        }
    }
    
    public static YamlConfiguration getKapiConfig(Kplugin plugin) {
        File dataFolder = plugin.getDataFolder();
        //noinspection ResultOfMethodCallIgnored
        dataFolder.mkdirs();
        File configFile = new File(dataFolder, "kapi.yml");
        try {
            boolean created = configFile.createNewFile();
            if (created) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                config.set("license", "PUT YOUR LICENSE KEY HERE");
                config.set("server", "78.46.110.72");
                config.set("port", 50008);
                config.set(
                        "publicKey",
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq3PnBCFuL0aoIikYcChuixkUg0vQTHX6Un5/kCg" +
                                "/UzIlTX7f2PHEf0WFj/OhSldOr93abIOMM8SFF/0q45MmltrdNe0QjW" +
                                "+vhF5cEKTWjSuWGAGWRO25r0il2dmfdYPmGbbp+gilkk6hpdnSDrRAsx/Rm/mAlnhjjf" +
                                "+WmSC6K1bVgJZSfO4tmZVq6IX6MpcddnD9KEQoBXsxywKeXU+rlXbQ1k2HgNS7giMirR" +
                                "/3TdJVUbGqxM5abmVSt5ecv62tmtkrcECTukFe922mh9SE0CGvCjLAwyXKYOm2uUbjKxcycyLDZMlDgNyWtsvo0lnkkDt4zIgsVBFXShR5i2ZxAwIDAQAB"
                );
                config.save(configFile);
            }
        } catch (IOException e) {
            System.out.println("Failed to create Kapi configuration file due to IOException");
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static Class<?> askServerForClass(Kplugin plugin) {
        final String name = plugin.getPluginName();
        YamlConfiguration config = getKapiConfig(plugin);
        String license = config.getString("license");
        String server = config.getString("server");
        String serverPublicKey = config.getString("publicKey");
        int port = config.getInt("port");
        
        if (serverPublicKey == null || serverPublicKey.isEmpty()) {
            System.out.println(
                    "Kapi server public key not found! Please set the server public key in the " +
                            "kapi.yml file.");
            return null;
        }
        
        if (license == null || license.isEmpty() || license.equals("PUT YOUR LICENSE KEY HERE")) {
            System.out.println(
                    "Kapi license not found! Please set your license key in the kapi.yml file.");
            return null;
        }
        Kplugin.userLicense = license;
        
        if (server == null || server.isEmpty()) {
            System.out.println(
                    "Kapi server not found! Please set the server IP in the kapi.yml file.");
            return null;
        }
        
        try (
                Socket socket = new Socket(server, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            KeyPair keyPair = CryptoUtils.generateRsaKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            String publicKey = CryptoUtils.publicKeyToString(keyPair.getPublic()).unwrap();
            String salt = UUID.randomUUID().toString();
            String hashedPublicKey = CryptoUtils.hashString(publicKey + salt);
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            String mac = Base64.getEncoder().encodeToString(ni.getHardwareAddress());
            String payload = salt + ":" + hashedPublicKey + ":" +
                    plugin.getKapiDeveloperLicense() + ":" + license + ":" +
                    mac + ":" + plugin.getPluginName();
            String encryptedPayload = CryptoUtils.encryptRsa(
                    payload,
                    CryptoUtils.stringToPublicKey(serverPublicKey).unwrap()
            ).unwrap();
            String message = publicKey + ":" + encryptedPayload;
            out.println(message);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            String[] split = response.toString().split(":");
            String encryptedSecret = split[0];
            String encryptedResponse = split[1];
            String secret = CryptoUtils.decryptRsa(encryptedSecret, privateKey).unwrap();
            split = secret.split(":");
            String ivString = split[0];
            String aesKeyString = split[1];
            IvParameterSpec iv = CryptoUtils.stringToIv(ivString).unwrap();
            SecretKey aesKey = CryptoUtils.stringToAesKey(aesKeyString).unwrapOrThrow();
            String responsePayload = CryptoUtils.decryptAes(encryptedResponse, aesKey, iv).unwrap();
            split = responsePayload.split(":");
            String verifiedResponse = split[0];
            String signedHashedResponse = split[1];
            String hashedResponse = CryptoUtils.verifyRsa(
                    signedHashedResponse,
                    CryptoUtils.stringToPublicKey(serverPublicKey).unwrap()
            ).unwrap();
            if (!CryptoUtils.hashString(verifiedResponse + salt).equals(hashedResponse)) {
                return null;
            }
            if (!verifiedResponse.toLowerCase().startsWith("bytecode")) {
                return null;
            }
            String base64 = verifiedResponse.substring("bytecode".length());
            byte[] classBytes = Base64.getDecoder().decode(base64);
            ByteClassLoader loader = new ByteClassLoader();
            return loader.defineClass(name + ".me.kyren223.kapi.core.KapiInit", classBytes);
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        } catch (Exception ignored) {
            System.err.println("Error Loading Kapi");
        }
        return null;
    }
    
    private static class ByteClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
